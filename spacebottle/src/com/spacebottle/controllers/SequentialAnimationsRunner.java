package com.spacebottle.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;

public class SequentialAnimationsRunner {
	 // アニメーションを走らせる画面
    private Activity target_activity = null;

    // アニメーション詳細設定たち
    private List<AnimationDescription> descriptions = new ArrayList<AnimationDescription>();

    // 全終了後のリスナ
    private AnimationsFinishListener animationsFinishListener = null;

    // アニメーション適用対象Viewたち
    private ArrayList<View> current_target_views = new ArrayList<View>();

    // 現在取り扱い中の詳細設定のインデックス
    private int current_description_cursor = 0;

    // １スレッドを使いまわすサービス
    private ExecutorService exService = null;

    // 全アニメーションを実行途中であるかどうか（簡易ロック用）
    private boolean executing_flag;


    // ------- 初期化処理 --------


    /**
     * 初期化
     */
    public SequentialAnimationsRunner(Activity activity)
    {
        this.target_activity = activity;
    }


    /**
     * アニメーション詳細設定たちを追加。
     * 可変長引数で何個でも可能。
     */
    public SequentialAnimationsRunner add( AnimationDescription...descs )
    {
        for( AnimationDescription desc : descs )
        {
            descriptions.add( desc );

            Log.d("AnimTest", descriptions.size() + "個目のdescriptionがaddされました。");
        }
        return this;
    }


    /**
     * アニメーション終了時の挙動を設定。
     */
    public SequentialAnimationsRunner onFinish(AnimationsFinishListener animationsFinishListener)
    {
        this.animationsFinishListener = animationsFinishListener;

        Log.d("AnimTest", "runnerにanimationsFinishListenerがセットされました。");
        return this;
    }


    // ------- 全Descriptionのスキャン処理 --------


    /**
     * 全アニメーションを開始する。
     */
    public void start()
    {
        // 開始済み？
        if( executionAlreadyStarted() )
        {
            // ランナーのインスタンス単位で排他する。
            Log.d("AnimTest", "このインスタンスのアニメーションは既に開始済みです。");
        }
        else
        {
            executing_flag = true;

            // 全部実行開始
            execAllDescriptions();
        }
    }


    /**
     * 登録された全詳細を実行開始
     */
    private void execAllDescriptions()
    {
        // NOTE: 1個以上の追加は前提とする

        // アニメーションはパフォーマンスを気にすべき処理なので
        // シングルスレッドを使いまわして毎回のスレッド生成のオーバーヘッドを省く
        exService = Executors.newSingleThreadExecutor();
            // @see http://www.techscore.com/tech/Java/JavaSE/Thread/7-2/
            // https://gist.github.com/1764033

        // カーソルを先頭にセット
        current_description_cursor = 0;
        Log.d("AnimTest", "最初の詳細を実行開始します。");

        // 開始
        execCurrentDescription();
    }


    /**
     * 現在のカーソルが指し示すアニメーション詳細設定を実行する。
     */
    private void execCurrentDescription()
    {
        // 現在のDescriptionを取得
        AnimationDescription desc = descriptions.get(current_description_cursor);

        // 現在のターゲットとなるViewを覚えさせる
        updateTargetsIfSpecified(desc);

        // 具体的なAnimationの指示が返されたか
        final Animation anim = desc.describe();
        if( anim != null )
        {
            Log.d("AnimTest", "実行すべきAnimationが返されました。");
            executeDescribedAnimation(desc, anim);
        }
        else
        {
            // 元スレッドに終了を通知
            Log.d("AnimTest", "実行すべきアニメーションは無かったため，ランナーに１ステップの終了を通知します。");
            onCurrentDescriptionFinished();
        }
    }


    // ------- 個別のアニメーション実行処理 --------


    /**
     * １つのアニメーションまたはAnimationSetを実行
     */
    private void executeDescribedAnimation(
        final AnimationDescription desc,
        final Animation anim
    )
    {
        // スレッド生成のコストを省きつつ，別スレッドでアニメを開始。
        // NOTE: 別スレッドに分ける理由は待機処理などが入るから。
        exService.execute(new Runnable(){
            @Override
            public void run() {
                // 別スレッドでアニメーションを実行
                carryAnimationFlowOnOtherThread(desc, anim);
            }
        });
    }


    /**
     * １アニメーション描画のメインの処理フロー。
     * 別スレッド上で実行される。
     */
    protected void carryAnimationFlowOnOtherThread(
        final AnimationDescription desc,
        final Animation anim
    )
    {
        // 事前待機処理を実行
        desc.execWaitBefore();

        // 全ターゲットViewでアニメ実行
        kickOneAnimationForAllTargetViews(desc, anim);

        // アニメ実行時間＋事後待機時間の分だけ，このスレッドは待つ
        desc.execWaitDuration();
        desc.execWaitAfter();

        // 全ターゲットViewで事後処理を実行
        modifyAfterForAllTargetViews(desc);

        // 元スレッドに終了を通知
        onCurrentDescriptionFinished();
    }


    /**
     * 全ターゲットViewでアニメーションを実行
     */
    private void kickOneAnimationForAllTargetViews(
        AnimationDescription desc,
        final Animation anim
    )
    {
        // Animationにdurationをセット
        if( desc.anim_duration > 0 )
        {
            anim.setDuration( desc.anim_duration );
        }

        // NOTE: アニメーション前後で効果が続くようにしたい（連続実行を前提とするので）
        // しかし，下記のメソッドは機能しない。
        anim.setFillEnabled(true);
        //anim.setFillBefore(true);
        anim.setFillAfter(true);
            // @see http://graphics-geek.blogspot.jp/2011/08/mysterious-behavior-of-fillbefore.html
            // "When fillEnabled is true, the value of fillBefore will be taken into account"
        // アニメーション終了後の状態を確実に保つためには，終了タイミングで属性をアニメどおりに変化させるしかない。
            // @see http://www.androiddiscuss.com/1-android-discuss/75731.html
            // http://stackoverflow.com/questions/3345084/how-can-i-animate-a-view-in-android-and-have-it-stay-in-the-new-position-size


        // 個々のターゲットViewごとに，具体的なAnimationを実行開始
        for( final View v : current_target_views )
        {
            Log.d("AnimTest", "アニメーションの開始を登録します。");

            // UI上の処理なので，UIスレッドにゆだねる
            target_activity.runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    Log.d("AnimTest", "アニメーションを開始します。");

                    // キック
                    v.startAnimation(anim);
                }
            });
        }
            // UIスレッドでキックしておいたアニメーションはこのまま放任しておく。
            // 終了のリスナなどもセットせず，このスレッドからはもう関知しない。
    }


    /**
     * 全ターゲットViewで事後処理を実行
     */
    private void modifyAfterForAllTargetViews(final AnimationDescription desc)
    {
        for( final View v : current_target_views )
        {
            // 属性変更が主なので，UIスレッドに頼む
            target_activity.runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    // このViewに対する事後処理
                    desc.modifyAfterAnimation(v);
                }
            });
        }
    }


    // ------- 複数アニメーションの制御 --------


    /**
     * アニメーションの実行が開始しているかどうか
     */
    private boolean executionAlreadyStarted()
    {
        return executing_flag;
    }


    /**
     * １つ分の詳細を扱い終わった際に呼ばれる。
     */
    protected void onCurrentDescriptionFinished()
    {
        Log.d("AnimTest", "１ステップのアニメーション実行完了時点として，ランナーに終了を通知します。");

        // これで全部終わりか判定
        if( allDescriptionsFinished() )
        {
            Log.d("AnimTest", "全詳細の扱いが終了しました。");

            // 全部実行終了
            target_activity.runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    // 事後処理
                    Log.d("AnimTest", "全詳細の事後処理を開始します。");
                    afterAllExecuted();
                }
            });

            executing_flag = false;
        }
        else
        {
            // 次の詳細へ
            execNextDescription();
        }
    }


    /**
     * 全詳細を扱い終えたかどうか判定
     */
    private boolean allDescriptionsFinished()
    {
        return ( current_description_cursor == ( descriptions.size() - 1 ) );
    }


    /**
     * 次の詳細を実行する
     */
    private void execNextDescription()
    {
        // カーソルをインクリメント
        current_description_cursor ++;

        Log.d("AnimTest", "次の詳細へ進みます。現在のカーソルは" + current_description_cursor);

        // 開始
        execCurrentDescription();
    }


    /**
     * もし必要なら，詳細の指示通りに，アニメーション適用対象を変更する。
     */
    private void updateTargetsIfSpecified(AnimationDescription desc)
    {
        // ターゲットが変更されたか
        if( desc.new_target_views != null )
        {
            // まず全部クリア
            current_target_views.clear();

            // １個ずつ登録しなおす
            for( View target_view : desc.new_target_views )
            {
                current_target_views.add( target_view );
            }

            Log.d("AnimTest", "ターゲットが変更されました。個数は" + current_target_views.size() );
        }
    }


    /**
     * 全アニメーション終了後の処理をUIスレッド上で実行
     */
    private void afterAllExecuted()
    {
        // 登録されていれば
        if( animationsFinishListener != null )
        {
            animationsFinishListener.exec();
        }
    }

}