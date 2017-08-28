package chen.com.demoandroid;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

  MyView myView;
  WindowManager wm;
  private WindowManager.LayoutParams windowManagerParams;
  private int originWidth;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    originWidth = 100;
    wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
    //设置TextView的属性
    windowManagerParams = new WindowManager.LayoutParams();
    windowManagerParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
    windowManagerParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
    //这里是关键，使控件始终在最上方
    windowManagerParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT | WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
    windowManagerParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
    //这个Gravity也不能少，不然的话，下面"移动歌词"的时候就会出问题了～ 可以试试[官网文档有说明]
    windowManagerParams.gravity = Gravity.LEFT | Gravity.TOP;

    //创建自定义的TextView
    myView = new MyView(this);
    myView.setText("Test Touch");
    myView.setTextColor(Color.BLACK);
    myView.setBackgroundColor(Color.WHITE);
    //监听 OnTouch 事件 为了实现"移动歌词"功能
    myView.setOnTouchListener(this);

    wm.addView(myView, windowManagerParams);
    showInRight();

  }

  /**
   * 悬浮按钮显示在左边
   */
  private void showInLeft() {
    windowManagerParams.x = 0;
    windowManagerParams.width = originWidth / 2;
    windowManagerParams.height = originWidth;
//    setImageResource(Res.drawable(context, "ipay_float_btn_left_hidden"));
    wm.updateViewLayout(myView, windowManagerParams); // 刷新显示
  }

  /**
   * 悬浮按钮显示在右边
   */
  private void showInRight() {
    windowManagerParams.width = originWidth / 2;
    windowManagerParams.height = originWidth;
    windowManagerParams.x = 200 - windowManagerParams.width;
//    setImageResource(Res.drawable(context, "ipay_float_btn_right_hidden"));
    wm.updateViewLayout(myView, windowManagerParams); // 刷新显示
  }

  /**
   * 悬浮按钮显示在上面
   */
  private void showInTop() {
    windowManagerParams.y = 0;
    windowManagerParams.width = originWidth;
    windowManagerParams.height = originWidth / 2;
//    setImageResource(Res.drawable(context, "ipay_float_btn_top_hidden"));
    wm.updateViewLayout(myView, windowManagerParams); // 刷新显示
  }

  /**
   * 悬浮按钮显示在下面
   */
  private void showInBottom() {
    windowManagerParams.width = originWidth;
    windowManagerParams.height = originWidth / 2;
    windowManagerParams.y = 200 - windowManagerParams.width;
//    setImageResource(Res.drawable(context, "ipay_float_btn_bottom_hidden"));
    wm.updateViewLayout(myView, windowManagerParams); // 刷新显示
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

    if (null != myView) {
      myView.setVisibility(View.GONE);
    }

  }

  @Override
  public boolean onTouch(View v, MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_UP:
        //getRawX/Y 是获取相对于Device的坐标位置 注意区别getX/Y[相对于View]
        windowManagerParams.x = (int) event.getRawX();
        windowManagerParams.y = (int) event.getRawY();
        //更新"桌面歌词"的位置
        wm.updateViewLayout(myView, windowManagerParams);
        //下面的removeView 可以去掉"桌面歌词"
        //wm.removeView(myView);
        break;
      case MotionEvent.ACTION_MOVE:
        windowManagerParams.x = (int) event.getRawX();
        windowManagerParams.y = (int) event.getRawY();
        wm.updateViewLayout(myView, windowManagerParams);
        break;
    }
    return false;
  }

  //继承 TextView  好吧，貌似有点多此一举，其实直接用TextView就好
  public class MyView extends AppCompatTextView {

    public MyView(Context context) {
      super(context);
    }


  }
}
