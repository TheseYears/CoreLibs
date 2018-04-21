package com.corelibs.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.corelibs.R;

@SuppressLint("InflateParams")
public class LoadingDialog extends Dialog {

	private static int theme = R.style.dialog;
	private Context context;
    private CircularBar bar;
	private TextView msg;

	public LoadingDialog(Context context) {
		super(context, theme);
		this.context = context;
		init();
		setMessage(null);
	}

	public LoadingDialog(Context context, String message) {
		super(context, theme);
		this.context = context;
		init();
		setMessage(message);
	}

	public LoadingDialog(Context context, int message) {
		super(context, theme);
		this.context = context;
		init();
		setMessage(message);
	}

	private void init() {
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
		setContentView(contentView);
		setCanceledOnTouchOutside(false);

        bar = (CircularBar) contentView.findViewById(R.id.circle);
		msg = (TextView) contentView.findViewById(R.id.tv_msg);
	}


	public void setMessage(String message) {
		if (message == null) {
			if (msg.getVisibility() == View.VISIBLE)
				msg.setVisibility(View.GONE);
			return;
		}

		if (msg.getVisibility() == View.GONE)
			msg.setVisibility(View.VISIBLE);
		msg.setText(message);
	}

	public void setMessage(int message) {
		if (msg.getVisibility() == View.GONE)
			msg.setVisibility(View.VISIBLE);
		msg.setText(message);
	}

	@Override
	public void show() {
		super.show();
        bar.startAnimation();
	}

	@Override
	public void dismiss() {
        super.dismiss();
        bar.stopAnimation();
	}
}
