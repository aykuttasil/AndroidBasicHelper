package com.aykuttasil.androidbasichelperlib

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.google.android.material.snackbar.Snackbar
import com.joanzapata.iconify.IconDrawable
import com.joanzapata.iconify.Iconify
import com.joanzapata.iconify.fonts.FontAwesomeIcons
import com.joanzapata.iconify.fonts.FontAwesomeModule

class UiHelper {

    class UiDialog private constructor(var mContext: Context) {

        var materialDialog: MaterialDialog

        init {
            Iconify.with(FontAwesomeModule())
            materialDialog = MaterialDialog(mContext)
        }

        @JvmOverloads
        fun getIndeterminateDialog(title: String, content: String = "Lütfen Bekleyiniz..."): MaterialDialog {
            val dialog = MaterialDialog(mContext)
                    .title(text = title)
                    .message(text = content)
                    .customView(viewRes = R.layout.progress_dialog_horizontal_layout)
                    .cancelable(false)

            return dialog
        }

        fun getOKDialog(title: String, content: String, icon: Drawable?): MaterialDialog {
            return materialDialog
                    .title(text = title)
                    .message(text = content)
                    .cancelable(false)
                    .icon(drawable = icon
                            ?: IconDrawable(mContext, FontAwesomeIcons.fa_angle_right).actionBarSize().color(Color.GRAY))
                    .positiveButton(text = "Devam Et") { dialog ->
                        dialog.dismiss()
                    }
        }

        fun getOKCancelDialog(title: String, content: String, icon: Drawable?): MaterialDialog {
            return materialDialog
                    .title(text = title)
                    .message(text = content)
                    .cancelable(false)
                    .icon(drawable = icon
                            ?: IconDrawable(mContext, FontAwesomeIcons.fa_angle_right).actionBarSize().color(Color.GRAY))
                    .positiveButton(text = "Devam Et") { dialog ->
                        dialog.dismiss()
                    }

                    .negativeButton(text = "İptal Et") { dialog ->
                        dialog.dismiss()
                    }
        }

        fun getProgressDialog(title: String, content: String, icon: Drawable?): MaterialDialog {
            val dialog = materialDialog
                    .title(text = title)
                    .cancelable(false)
                    .icon(drawable = icon
                            ?: IconDrawable(mContext, FontAwesomeIcons.fa_angle_right).actionBarSize().color(Color.GRAY))
                    .customView(viewRes = R.layout.progress_dialog_layout)

            dialog.getCustomView().findViewById<TextView>(R.id.txtContent).text = content
            return dialog
        }


        fun getProgressDialog(content: String, icon: Drawable?): MaterialDialog {
            val dialog = materialDialog
                    .cancelable(false)
                    .icon(drawable = icon
                            ?: IconDrawable(mContext, FontAwesomeIcons.fa_angle_right).actionBarSize().color(Color.GRAY))

            dialog.getCustomView().findViewById<TextView>(R.id.txtContent).text = content
            return dialog
        }

        companion object {

            fun newInstance(context: Context): UiDialog {
                return UiDialog(context)
            }

            fun showSimpleDialog(context: Context, title: String, message: String) {
                newInstance(context).materialDialog.title(text = title).message(text = message).show()

            }
        }
    }

    //    public static class UiSweetDialog {
    //
    //        Context mContext;
    //        SweetAlertDialog mSweetAlertDialog;
    //
    //        private UiSweetDialog(Context context, int alertDialogType) {
    //            Iconify.with(new FontAwesomeModule());
    //            mContext = context;
    //            mSweetAlertDialog = new SweetAlertDialog(context, alertDialogType);
    //        }
    //
    //        public static UiSweetDialog newInstance(Context context, int alertDialogType) {
    //            return new UiSweetDialog(context, alertDialogType);
    //        }
    //
    //        public SweetAlertDialog getMaterialDialog() {
    //            try {
    //                if (mSweetAlertDialog == null) {
    //                    throw new Exception("mSweetAlertDialog is null");
    //                }
    //            } catch (Exception e) {
    //                e.printStackTrace();
    //            }
    //            return mSweetAlertDialog;
    //        }
    //
    //        public static void showSimpleDialog(Context context, String title, String message) {
    //
    //            newInstance(context, SweetAlertDialog.NORMAL_TYPE).mSweetAlertDialog.setTitleText(title).setContentText(message).show();
    //
    //        }
    //
    //        public SweetAlertDialog getIndeterminateDialog(String title, String content) {
    //            return mBuilder
    //                    .title(title)
    //                    .content(content)
    //                    .progress(true, 100, false)
    //                    .progressIndeterminateStyle(true)
    //                    .cancelable(false)
    //                    .icon(new IconDrawable(mContext, FontAwesomeIcons.fa_comment).actionBarSize().color(Color.GRAY))
    //                    .build();
    //        }
    //
    //        public SweetAlertDialog getOKDialog(String title, String content, Drawable icon) {
    //            return mBuilder
    //                    .title(title)
    //                    .content(content)
    //                    .cancelable(false)
    //                    .icon(icon == null ? new IconDrawable(mContext, FontAwesomeIcons.fa_angle_right).actionBarSize().color(Color.GRAY) : icon)
    //                    .onPositive(new MaterialDialog.SingleButtonCallback() {
    //                        @Override
    //                        public void onClick(MaterialDialog dialog, DialogAction which) {
    //                            dialog.dismiss();
    //                        }
    //                    })
    //                    .positiveText("Devam Et")
    //                    .build();
    //        }
    //
    //        public SweetAlertDialog getOKCancelDialog(final String title, String content, final String whenClickOkText, Drawable icon) {
    //
    //            mSweetAlertDialog.setTitleText(title);
    //            mSweetAlertDialog.setContentText(content);
    //            mSweetAlertDialog.setCancelable(false);
    //            mSweetAlertDialog.setCustomImage(icon);
    //            mSweetAlertDialog.setCancelText("İptal Et");
    //            mSweetAlertDialog.setConfirmText("Devam et");
    //
    //            mSweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
    //                @Override
    //                public void onClick(SweetAlertDialog sweetAlertDialog) {
    //                    sweetAlertDialog.dismiss();
    //                }
    //            });
    //
    //            mSweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
    //                @Override
    //                public void onClick(SweetAlertDialog sweetAlertDialog) {
    //                    sweetAlertDialog
    //                            .setTitleText(title)
    //                            .setContentText(whenClickOkText)
    //                            .showCancelButton(false)
    //                            .setConfirmText("Tamam")
    //                            .setConfirmClickListener(null)
    //                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
    //                }
    //            });
    //
    //            mSweetAlertDialog.show();
    //
    //            return mSweetAlertDialog;
    //
    //
    //            return mBuilder
    //                    .title(title)
    //                    .content(content)
    //                    .cancelable(false)
    //                    .icon(icon == null ? new IconDrawable(mContext, FontAwesomeIcons.fa_angle_right).actionBarSize().color(Color.GRAY) : icon)
    //                    .onPositive(new MaterialDialog.SingleButtonCallback() {
    //                        @Override
    //                        public void onClick(MaterialDialog dialog, DialogAction which) {
    //                            dialog.dismiss();
    //                        }
    //                    })
    //                    .onNegative(new MaterialDialog.SingleButtonCallback() {
    //                        @Override
    //                        public void onClick(MaterialDialog dialog, DialogAction which) {
    //                            dialog.dismiss();
    //                        }
    //                    })
    //                    .negativeText("İptal Et")
    //                    .positiveText("Devam Et")
    //                    .negativeColor(Color.GRAY)
    //                    .positiveColor(mContext.getResources().getColor(R.color.accent))
    //                    .build();
    //        }
    //
    //        public SweetAlertDialog getProgressDialog(String title, String content, Drawable icon) {
    //
    //            mSweetAlertDialog.setTitleText(title);
    //            mSweetAlertDialog.setContentText(content);
    //            mSweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
    //            mSweetAlertDialog.setCancelable(false);
    //            mSweetAlertDialog.show();
    //
    //            return mSweetAlertDialog;
    //        }
    //
    //        public SweetAlertDialog getProgressDialog(String content, Drawable icon) {
    //
    //            mSweetAlertDialog.setContentText(content);
    //            mSweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
    //            mSweetAlertDialog.setCancelable(false);
    //            mSweetAlertDialog.show();
    //
    //            return mSweetAlertDialog;
    //        }
    //    }

    class UiSnackBar private constructor(view: View, text: String?, duration: Int) {
        var snackbar: Snackbar
            internal set

        init {
            snackbar = Snackbar.make(view, text ?: "", duration)
        }

        fun setText(text: String): UiSnackBar {
            snackbar.setText(text)
            return this
        }

        fun setDuration(duration: Int): UiSnackBar {
            snackbar.duration = duration
            return this
        }

        fun setBackgroundColor(color: Int): UiSnackBar {
            snackbar.view.setBackgroundColor(color)
            return this
        }

        fun setActionTextColor(color: Int): UiSnackBar {
            snackbar.setActionTextColor(color)
            return this
        }

        fun setMessageTextColor(color: Int): UiSnackBar {
            val textView = snackbar.view.findViewById<View>(R.id.snackbar_text) as TextView
            textView.setTextColor(color)
            return this
        }

        fun show() {
            snackbar.show()
        }

        companion object {

            fun newInstance(view: View, text: String, duration: Int): Snackbar {
                return UiSnackBar(view, text, duration).snackbar
            }

            fun newInstance(context: Context, view: View): UiSnackBar {
                return UiSnackBar(view, null, Snackbar.LENGTH_LONG)
            }

            fun showSimpleSnackBar(view: View?, text: String, duration: Int) {
                if (view == null) return

                val snackbar = Snackbar.make(view, text, duration)
                val textView = snackbar.view.findViewById<TextView>(R.id.snackbar_text)
                textView.setTextColor(Color.WHITE)

                snackbar.show()
            }

            private fun findSuitableParent(view: View?): ViewGroup? {
                var view = view
                var fallback: ViewGroup? = null
                do {
                    if (view is CoordinatorLayout) {
                        // We've found a CoordinatorLayout, use it
                        return view
                    } else if (view is FrameLayout) {
                        if (view.id == android.R.id.content) {
                            // If we've hit the decor content view, then we didn't find a CoL in the
                            // hierarchy, so use it.
                            return view
                        } else {
                            // It's not the content view but we'll use it as our fallback
                            fallback = view
                        }
                    }

                    if (view != null) {
                        // Else, we will loop and crawl up the view hierarchy and try to find a parent
                        val parent = view.parent
                        view = if (parent is View) parent else null
                    }
                } while (view != null)

                // If we reach here then we didn't find a CoL or a suitable content view so we'll fallback
                return fallback
            }
        }

    }

    class UiToast private constructor(context: Context, text: String?, duration: Int) {

        var toast: Toast

        init {
            toast = Toast.makeText(context, text, duration)
        }

        fun setText(text: String): UiToast {
            toast.setText(text)
            return this
        }

        fun setDuration(duration: Int): UiToast {
            toast.duration = duration
            return this
        }

        fun setBackgroundColor(color: Int): UiToast {
            toast.view.setBackgroundColor(color)
            return this
        }

        fun show() {
            toast.show()
        }

        companion object {

            fun newInstance(context: Context, text: String, duration: Int): Toast {
                return UiToast(context, text, duration).toast
            }

            fun newInstance(context: Context): UiToast {
                return UiToast(context, null, Toast.LENGTH_LONG)
            }

            fun showSimpleToast(context: Context, text: String, duration: Int) {
                Toast.makeText(context, text, duration).show()

            }
        }
    }
}
