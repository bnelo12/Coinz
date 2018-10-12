package elosoft.coinz.Components;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import elosoft.coinz.R;

public class EightBitRetroKeyBoard extends RelativeLayout {
    private TextEmitter textEmitter;

    public EightBitRetroKeyBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.keyboard, this);
        init();
    }

    private void init() {
        TextView one_key = this.findViewById(R.id.key_1);
        TextView two_key = this.findViewById(R.id.key_2);
        TextView three_key = this.findViewById(R.id.key_3);
        TextView four_key = this.findViewById(R.id.key_4);
        TextView five_key = this.findViewById(R.id.key_5);
        TextView six_key = this.findViewById(R.id.key_6);
        TextView seven_key = this.findViewById(R.id.key_7);
        TextView eight_key = this.findViewById(R.id.key_8);
        TextView nine_key = this.findViewById(R.id.key_9);
        TextView zero_key = this.findViewById(R.id.key_0);
        TextView q_key = this.findViewById(R.id.key_q);
        TextView w_key = this.findViewById(R.id.key_w);
        TextView e_key = this.findViewById(R.id.key_e);
        TextView r_key = this.findViewById(R.id.key_r);
        TextView t_key = this.findViewById(R.id.key_t);
        TextView y_key = this.findViewById(R.id.key_y);
        TextView u_key = this.findViewById(R.id.key_u);
        TextView i_key = this.findViewById(R.id.key_i);
        TextView o_key = this.findViewById(R.id.key_o);
        TextView p_key = this.findViewById(R.id.key_p);
        TextView a_key = this.findViewById(R.id.key_a);
        TextView s_key = this.findViewById(R.id.key_s);
        TextView d_key = this.findViewById(R.id.key_d);
        TextView f_key = this.findViewById(R.id.key_f);
        TextView g_key = this.findViewById(R.id.key_g);
        TextView h_key = this.findViewById(R.id.key_h);
        TextView j_key = this.findViewById(R.id.key_j);
        TextView k_key = this.findViewById(R.id.key_k);
        TextView l_key = this.findViewById(R.id.key_l);
        TextView z_key = this.findViewById(R.id.key_z);
        TextView x_key = this.findViewById(R.id.key_x);
        TextView c_key = this.findViewById(R.id.key_c);
        TextView v_key = this.findViewById(R.id.key_v);
        TextView b_key = this.findViewById(R.id.key_b);
        TextView n_key = this.findViewById(R.id.key_n);
        TextView m_key = this.findViewById(R.id.key_m);
        TextView done_key = this.findViewById(R.id.key_done);
        TextView back_key = this.findViewById(R.id.key_back);

        OnClickListener handleKeyPress = new OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv =  (TextView) v;
                textEmitter.addString(tv.getText().toString());
            }
        };

        OnClickListener handleBackKeyPress = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textEmitter.userInputMode) {
                    textEmitter.backSpace();
                }
            }
        };

        OnClickListener handleDoneKeyPress = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textEmitter.userInputMode) {
                    textEmitter.addUserInputAndContinue();
                }
            }
        };

        done_key.setOnClickListener(handleDoneKeyPress);
        back_key.setOnClickListener(handleBackKeyPress);
        one_key.setOnClickListener(handleKeyPress);
        two_key.setOnClickListener(handleKeyPress);
        three_key.setOnClickListener(handleKeyPress);
        four_key.setOnClickListener(handleKeyPress);
        five_key.setOnClickListener(handleKeyPress);
        six_key.setOnClickListener(handleKeyPress);
        seven_key.setOnClickListener(handleKeyPress);
        eight_key.setOnClickListener(handleKeyPress);
        nine_key.setOnClickListener(handleKeyPress);
        zero_key.setOnClickListener(handleKeyPress);
        q_key.setOnClickListener(handleKeyPress);
        w_key.setOnClickListener(handleKeyPress);
        e_key.setOnClickListener(handleKeyPress);
        r_key.setOnClickListener(handleKeyPress);
        t_key.setOnClickListener(handleKeyPress);
        y_key.setOnClickListener(handleKeyPress);
        u_key.setOnClickListener(handleKeyPress);
        i_key.setOnClickListener(handleKeyPress);
        o_key.setOnClickListener(handleKeyPress);
        p_key.setOnClickListener(handleKeyPress);
        a_key.setOnClickListener(handleKeyPress);
        s_key.setOnClickListener(handleKeyPress);
        d_key.setOnClickListener(handleKeyPress);
        f_key.setOnClickListener(handleKeyPress);
        g_key.setOnClickListener(handleKeyPress);
        h_key.setOnClickListener(handleKeyPress);
        j_key.setOnClickListener(handleKeyPress);
        k_key.setOnClickListener(handleKeyPress);
        l_key.setOnClickListener(handleKeyPress);
        z_key.setOnClickListener(handleKeyPress);
        x_key.setOnClickListener(handleKeyPress);
        c_key.setOnClickListener(handleKeyPress);
        v_key.setOnClickListener(handleKeyPress);
        b_key.setOnClickListener(handleKeyPress);
        n_key.setOnClickListener(handleKeyPress);
        m_key.setOnClickListener(handleKeyPress);
    }

    public void addTextEmitter(TextEmitter textEmitter) {
        this.textEmitter = textEmitter;
    }
}
