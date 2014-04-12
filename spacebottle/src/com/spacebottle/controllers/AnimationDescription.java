package com.spacebottle.controllers;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;

public class AnimationDescription {
	// アニメーションの適用対象
    public View[] new_target_views = null;

    // 開始前の待機時間
    public int wait_before = 0;

    // アニメーションの持続時間
    public int anim_duration = 0;

    // 終了後の待機時間
    public int wait_after = 0;



    // --------- ユーザ定義用 -----------


    /**
     * 具体的なアニメーションを定義。
     * AnimationまたはAnimationSetを返すこと。
     */
    protected Animation describe() {
        // Override me
        return null;
    }


    /**
     * アニメーション後に各種属性を変更（setFillAfter()が効かない問題への対処）
     */
    protected void modifyAfterAnimation(View v) {
        // Override me
    }


    // --------- setter -----------


    /**
     * アニメーションの適用対象をセット
     */
    public AnimationDescription targetViews(View...views) {
        this.new_target_views = views;
        return this;
    }


    /**
     * アニメーション開始前の待機時間をセット
     */
    public AnimationDescription waitBefore(int milli_sec) {
        this.wait_before = milli_sec;
        return this;
    }


    /**
     * アニメーションのdurationをセット
     */
    public AnimationDescription duration(int milli_sec) {
        this.anim_duration = milli_sec;
        return this;
    }


    /**
     * アニメーション終了後の待機時間をセット
     */
    public AnimationDescription waitAfter(int milli_sec) {
        this.wait_after = milli_sec;
        return this;
    }


    // --------- 待機処理を実行 -----------


    /**
     * 事前待機処理
     */
    public void execWaitBefore()
    {
        if( wait_before > 0 )
        {
            Log.d("AnimTest", "事前待機処理を実行します。");
            sleepMS(wait_before);
        }
    }


    /**
     * 事後待機処理
     */
    public void execWaitAfter()
    {
        if( wait_after > 0 )
        {
            Log.d("AnimTest", "事後待機処理を実行します。");
            sleepMS(wait_after);
        }
    }


    /**
     * アニメーション実行中の待機処理
     */
    public void execWaitDuration() {
        Log.d("AnimTest", "duration分の待機処理を実行します。");
        sleepMS(anim_duration);
    }


    /**
     * 指定されたミリ秒だけスリープ
     */
    private void sleepMS(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignore) {
        }
    }
}
