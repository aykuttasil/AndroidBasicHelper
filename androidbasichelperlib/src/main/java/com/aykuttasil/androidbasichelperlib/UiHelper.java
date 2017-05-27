package com.aykuttasil.androidbasichelperlib;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class UiHelper {

    public static class UiDialog {

        MaterialDialog dialog;
        MaterialDialog.Builder mBuilder;
        Context mContext;

        private UiDialog(Context context) {
            Iconify.with(new FontAwesomeModule());
            mContext = context;
            mBuilder = new MaterialDialog.Builder(context);
            dialog = new MaterialDialog.Builder(context).build();
        }

        public static UiDialog newInstance(Context context) {
            return new UiDialog(context);
        }

        public MaterialDialog getMaterialDialog() {
            return dialog;
        }

        public MaterialDialog.Builder getMaterialDialogBuilder() {
            return mBuilder;
        }

        public static void showSimpleDialog(Context context, String title, String message) {
            newInstance(context).getMaterialDialogBuilder().title(title).content(message).autoDismiss(true).build().show();

        }

        public MaterialDialog getIndeterminateDialog(String title, String content) {
            return mBuilder
                    .title(title)
                    .content(content)
                    .progress(true, 100, false)
                    .progressIndeterminateStyle(true)
                    .cancelable(false)
                    .icon(new IconDrawable(mContext, FontAwesomeIcons.fa_comment).actionBarSize().color(Color.GRAY))
                    .build();
        }

        public MaterialDialog getOKDialog(String title, String content, Drawable icon) {
            return mBuilder
                    .title(title)
                    .content(content)
                    .cancelable(false)
                    .icon(icon == null ? new IconDrawable(mContext, FontAwesomeIcons.fa_angle_right).actionBarSize().color(Color.GRAY) : icon)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog dialog, DialogAction which) {
                            dialog.dismiss();
                        }
                    })
                    .positiveText("Devam Et")
                    .build();
        }

        public MaterialDialog getOKCancelDialog(String title, String content, Drawable icon) {
            return mBuilder
                    .title(title)
                    .content(content)
                    .cancelable(false)
                    .icon(icon == null ? new IconDrawable(mContext, FontAwesomeIcons.fa_angle_right).actionBarSize().color(Color.GRAY) : icon)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog dialog, DialogAction which) {
                            dialog.dismiss();
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog dialog, DialogAction which) {
                            dialog.dismiss();
                        }
                    })
                    .negativeText("İptal Et")
                    .positiveText("Devam Et")
                    .negativeColor(Color.GRAY)
                    .positiveColor(mContext.getResources().getColor(R.color.accent))
                    .build();
        }

        public MaterialDialog getProgressDialog(String title, String content, Drawable icon) {
            return mBuilder
                    .title(title)
                    .content(content)
                    .cancelable(false)
                    .icon(icon == null ? new IconDrawable(mContext, FontAwesomeIcons.fa_angle_right).actionBarSize().color(Color.GRAY) : icon)
                    .progress(true, 0)
                    .build();
        }

        public MaterialDialog getProgressDialog(String content, Drawable icon) {
            return mBuilder
                    .content(content)
                    .cancelable(false)
                    .icon(icon == null ? new IconDrawable(mContext, FontAwesomeIcons.fa_angle_right).actionBarSize().color(Color.GRAY) : icon)
                    .progress(true, 0)
                    .build();
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

    public static class UiSnackBar {
        static Context mContext;
        Snackbar snackbar;

        private UiSnackBar(View view, String text, int duration) {
            snackbar = Snackbar.make(view, text, duration);
        }

        public static Snackbar newInstance(View view, String text, int duration) {

            return new UiSnackBar(view, text, duration).snackbar;

        }

        public static UiSnackBar newInstance(Context context, View view) {
            mContext = context;
            return new UiSnackBar(view, null, Snackbar.LENGTH_LONG);

        }

        public static void showSimpleSnackBar(View view, String text, int duration) {
            Snackbar snackbar = Snackbar.make(view, text, duration);
            TextView textView = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);

            snackbar.show();

        }

        private static ViewGroup findSuitableParent(View view) {
            ViewGroup fallback = null;
            do {
                if (view instanceof CoordinatorLayout) {
                    // We've found a CoordinatorLayout, use it
                    return (ViewGroup) view;
                } else if (view instanceof FrameLayout) {
                    if (view.getId() == android.R.id.content) {
                        // If we've hit the decor content view, then we didn't find a CoL in the
                        // hierarchy, so use it.
                        return (ViewGroup) view;
                    } else {
                        // It's not the content view but we'll use it as our fallback
                        fallback = (ViewGroup) view;
                    }
                }

                if (view != null) {
                    // Else, we will loop and crawl up the view hierarchy and try to find a parent
                    final ViewParent parent = view.getParent();
                    view = parent instanceof View ? (View) parent : null;
                }
            } while (view != null);

            // If we reach here then we didn't find a CoL or a suitable content view so we'll fallback
            return fallback;
        }

        public UiSnackBar setText(String text) {
            snackbar.setText(text);
            return this;
        }

        public UiSnackBar setDuration(int duration) {
            snackbar.setDuration(duration);
            return this;
        }

        public UiSnackBar setBackgroundColor(int color) {
            snackbar.getView().setBackgroundColor(color);
            return this;
        }

        public UiSnackBar setActionTextColor(int color) {
            snackbar.setActionTextColor(color);
            return this;
        }

        public UiSnackBar setMessageTextColor(int color) {
            TextView textView = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
            textView.setTextColor(color);
            return this;
        }

        public Snackbar getSnackbar() {
            return snackbar;
        }

        public void show() {
            snackbar.show();
        }

    }

    public static class UiToast {

        static Context mContext;
        Toast toast;

        private UiToast(Context context, String text, int duration) {
            mContext = context;
            toast = Toast.makeText(context, text, duration);

        }

        public static Toast newInstance(Context context, String text, int duration) {
            mContext = context;
            return new UiToast(context, text, duration).toast;
        }

        public static UiToast newInstance(Context context) {
            return new UiToast(context, null, Toast.LENGTH_LONG);
        }

        public static void showSimpleToast(Context context, String text, int duration) {
            Toast.makeText(context, text, duration).show();

        }

        public UiToast setText(String text) {
            toast.setText(text);
            return this;
        }

        public UiToast setDuration(int duration) {
            toast.setDuration(duration);
            return this;
        }

        public UiToast setBackgroundColor(int color) {
            toast.getView().setBackgroundColor(color);
            return this;
        }

        public void show() {
            toast.show();
        }
    }
}
