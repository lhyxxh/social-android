package com.eastcom.social.pos.util.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eastcom.social.pos.R;

public class NumDialog extends Dialog {

    public NumDialog(Context context) {
        super(context);
    }

    public NumDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder implements android.view.View.OnClickListener {
        private Context context;
        private String title;
        private String positiveButtonText;
        private String negativeButtonText;
        private View contentView;
        private DialogInterface.OnClickListener positiveButtonClickListener;
        private DialogInterface.OnClickListener negativeButtonClickListener;
        private EditText et_sum;
        private TextView tv_1;
        private TextView tv_2;
        private TextView tv_3;
        private TextView tv_4;
        private TextView tv_5;
        private TextView tv_6;
        private TextView tv_7;
        private TextView tv_8;
        private TextView tv_9;
        private TextView tv_0;
        private TextView tv_delete;
        private LinearLayout lv_number;
        private TextView tv_change;
        private LinearLayout lv_english;
        private TextView tv_q, tv_w, tv_e, tv_r, tv_t, tv_y, tv_u, tv_i, tv_o, tv_p;
        private TextView tv_a, tv_s, tv_d, tv_f, tv_g, tv_h, tv_j, tv_k, tv_l;
        private TextView tv_z, tv_x, tv_c, tv_v, tv_b, tv_n, tv_m, tv_back;
        Boolean isNumber = true;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        // 获取部署点名称
        public String GetSum() {

            return et_sum.getText().toString();
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public NumDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final NumDialog dialog = new NumDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.view_num_dialog, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            // set the dialog title
            ((TextView) layout.findViewById(R.id.title)).setText(title);
            et_sum = (EditText) layout.findViewById(R.id.et_sum);
            tv_2 = (TextView) layout.findViewById(R.id.tv_2);
            tv_1 = (TextView) layout.findViewById(R.id.tv_1);
            tv_3 = (TextView) layout.findViewById(R.id.tv_3);
            tv_4 = (TextView) layout.findViewById(R.id.tv_4);
            tv_5 = (TextView) layout.findViewById(R.id.tv_5);
            tv_6 = (TextView) layout.findViewById(R.id.tv_6);
            tv_2.setOnClickListener(this);
            tv_3.setOnClickListener(this);
            tv_1.setOnClickListener(this);
            tv_5.setOnClickListener(this);
            tv_6.setOnClickListener(this);
            tv_4.setOnClickListener(this);
            tv_7 = (TextView) layout.findViewById(R.id.tv_7);
            tv_8 = (TextView) layout.findViewById(R.id.tv_8);
            tv_9 = (TextView) layout.findViewById(R.id.tv_9);
            tv_0 = (TextView) layout.findViewById(R.id.tv_0);
            tv_delete = (TextView) layout.findViewById(R.id.tv_delete);
            tv_7.setOnClickListener(this);
            tv_8.setOnClickListener(this);
            tv_9.setOnClickListener(this);
            tv_0.setOnClickListener(this);
            tv_delete.setOnClickListener(this);
            tv_change = (TextView) layout.findViewById(R.id.change);
            lv_number = (LinearLayout) layout.findViewById(R.id.lv_number);
            tv_change.setOnClickListener(this);
            lv_english = (LinearLayout) layout.findViewById(R.id.lv_english);
            lv_english.setOnClickListener(this);
            tv_q = (TextView) layout.findViewById(R.id.tv_q);
            tv_w = (TextView) layout.findViewById(R.id.tv_w);
            tv_e = (TextView) layout.findViewById(R.id.tv_e);
            tv_r = (TextView) layout.findViewById(R.id.tv_r);
            tv_t = (TextView) layout.findViewById(R.id.tv_t);
            tv_y = (TextView) layout.findViewById(R.id.tv_y);
            tv_u = (TextView) layout.findViewById(R.id.tv_u);
            tv_i = (TextView) layout.findViewById(R.id.tv_i);
            tv_o = (TextView) layout.findViewById(R.id.tv_o);
            tv_p = (TextView) layout.findViewById(R.id.tv_p);
            tv_a = (TextView) layout.findViewById(R.id.tv_a);
            tv_s = (TextView) layout.findViewById(R.id.tv_s);
            tv_d = (TextView) layout.findViewById(R.id.tv_d);
            tv_f = (TextView) layout.findViewById(R.id.tv_f);
            tv_g = (TextView) layout.findViewById(R.id.tv_g);
            tv_h = (TextView) layout.findViewById(R.id.tv_h);
            tv_j = (TextView) layout.findViewById(R.id.tv_j);
            tv_k = (TextView) layout.findViewById(R.id.tv_k);
            tv_l = (TextView) layout.findViewById(R.id.tv_l);
            tv_z = (TextView) layout.findViewById(R.id.tv_z);
            tv_x = (TextView) layout.findViewById(R.id.tv_x);
            tv_c = (TextView) layout.findViewById(R.id.tv_c);
            tv_v = (TextView) layout.findViewById(R.id.tv_v);
            tv_b = (TextView) layout.findViewById(R.id.tv_b);
            tv_n = (TextView) layout.findViewById(R.id.tv_n);
            tv_m = (TextView) layout.findViewById(R.id.tv_m);
            tv_back = (TextView)layout.findViewById(R.id.tv_back);
            tv_q.setOnClickListener(this);
            tv_w.setOnClickListener(this);
            tv_e.setOnClickListener(this);
            tv_r.setOnClickListener(this);
            tv_t.setOnClickListener(this);
            tv_y.setOnClickListener(this);
            tv_u.setOnClickListener(this);
            tv_i.setOnClickListener(this);
            tv_o.setOnClickListener(this);
            tv_p.setOnClickListener(this);
            tv_a.setOnClickListener(this);
            tv_s.setOnClickListener(this);
            tv_d.setOnClickListener(this);
            tv_f.setOnClickListener(this);
            tv_g.setOnClickListener(this);
            tv_h.setOnClickListener(this);
            tv_j.setOnClickListener(this);
            tv_k.setOnClickListener(this);
            tv_l.setOnClickListener(this);
            tv_z.setOnClickListener(this);
            tv_x.setOnClickListener(this);
            tv_c.setOnClickListener(this);
            tv_v.setOnClickListener(this);
            tv_b.setOnClickListener(this);
            tv_n.setOnClickListener(this);
            tv_m.setOnClickListener(this);
            tv_back.setOnClickListener(this);
            et_sum.setImeOptions(EditorInfo.IME_ACTION_DONE);
            et_sum.setInputType(InputType.TYPE_NULL);
            // set the confirm button
            if (positiveButtonText != null) {
                ((TextView) layout.findViewById(R.id.positiveButton))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    ((TextView) layout.findViewById(R.id.positiveButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.positiveButton).setVisibility(
                        View.GONE);
            }
            // set the cancel button
            if (negativeButtonText != null) {
                ((TextView) layout.findViewById(R.id.negativeButton))
                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((TextView) layout.findViewById(R.id.negativeButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.negativeButton).setVisibility(
                        View.GONE);
            }
            dialog.setContentView(layout);
            return dialog;
        }

        @Override
        public void onClick(View v) {
            String oldString = et_sum.getText().toString();
            int len = oldString.length();
            switch (v.getId()) {
                case R.id.tv_1:
                    et_sum.setText(oldString + "1");
                    break;
                case R.id.tv_2:
                    et_sum.setText(oldString + "2");
                    break;
                case R.id.tv_3:
                    et_sum.setText(oldString + "3");
                    break;
                case R.id.tv_4:
                    et_sum.setText(oldString + "4");
                    break;
                case R.id.tv_5:
                    et_sum.setText(oldString + "5");
                    break;
                case R.id.tv_6:
                    et_sum.setText(oldString + "6");
                    break;
                case R.id.tv_7:
                    et_sum.setText(oldString + "7");
                    break;
                case R.id.tv_8:
                    et_sum.setText(oldString + "8");
                    break;
                case R.id.tv_9:
                    et_sum.setText(oldString + "9");
                    break;
                case R.id.tv_0:
                    et_sum.setText(oldString + "0");
                    break;
                case R.id.tv_q:
                    et_sum.setText(oldString + "Q");
                    break;
                case R.id.tv_w:
                    et_sum.setText(oldString + "W");
                    break;
                case R.id.tv_e:
                    et_sum.setText(oldString + "E");
                    break;
                case R.id.tv_r:
                    et_sum.setText(oldString + "R");
                    break;
                case R.id.tv_t:
                    et_sum.setText(oldString + "T");
                    break;
                case R.id.tv_y:
                    et_sum.setText(oldString + "Y");
                    break;
                case R.id.tv_u:
                    et_sum.setText(oldString + "U");
                    break;
                case R.id.tv_i:
                    et_sum.setText(oldString + "I");
                    break;
                case R.id.tv_o:
                    et_sum.setText(oldString + "O");
                    break;
                case R.id.tv_p:
                    et_sum.setText(oldString + "P");
                    break;
                case R.id.tv_a:
                    et_sum.setText(oldString + "A");
                    break;
                case R.id.tv_s:
                    et_sum.setText(oldString + "S");
                    break;
                case R.id.tv_d:
                    et_sum.setText(oldString + "D");
                    break;
                case R.id.tv_f:
                    et_sum.setText(oldString + "F");
                    break;
                case R.id.tv_g:
                    et_sum.setText(oldString + "G");
                    break;
                case R.id.tv_h:
                    et_sum.setText(oldString + "H");
                    break;
                case R.id.tv_j:
                    et_sum.setText(oldString + "J");
                    break;
                case R.id.tv_k:
                    et_sum.setText(oldString + "K");
                    break;
                case R.id.tv_l:
                    et_sum.setText(oldString + "L");
                    break;
                case R.id.tv_z:
                    et_sum.setText(oldString + "Z");
                    break;
                case R.id.tv_x:
                    et_sum.setText(oldString + "X");
                    break;
                case R.id.tv_c:
                    et_sum.setText(oldString + "C");
                    break;
                case R.id.tv_v:
                    et_sum.setText(oldString + "V");
                    break;
                case R.id.tv_b:
                    et_sum.setText(oldString + "B");
                    break;
                case R.id.tv_n:
                    et_sum.setText(oldString + "N");
                    break;
                case R.id.tv_m:
                    et_sum.setText(oldString + "M");
                    break;
                case R.id.tv_back:
                    if (len > 0) {
                        et_sum.setText(oldString.substring(0, len - 1));
                    }
                    break;

                case R.id.tv_delete:
                    if (len > 0) {
                        et_sum.setText(oldString.substring(0, len - 1));
                    }
                    break;
                case R.id.change:
                    isNumber = !isNumber;
                    if (isNumber) {
                        lv_number.setVisibility(View.VISIBLE);
                        lv_english.setVisibility(View.GONE);
                    } else {
                        lv_number.setVisibility(View.GONE);
                        lv_english.setVisibility(View.VISIBLE);
                    }
                    break;
                default:
                    break;
            }
            String newString = et_sum.getText().toString();
            et_sum.setSelection(newString.length());
        }
    }
}
