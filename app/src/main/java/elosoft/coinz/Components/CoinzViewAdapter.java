package elosoft.coinz.Components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import elosoft.coinz.Models.Coin;
import elosoft.coinz.Models.DrawableCoin;
import elosoft.coinz.R;

public class CoinzViewAdapter extends BaseAdapter {

    private final Context appContext;
    public final ArrayList<DrawableCoin> coinz;

    public CoinzViewAdapter(Context context, ArrayList<DrawableCoin> coinz) {
        this.appContext = context;
        this.coinz = coinz;
    }

    @Override
    public int getCount() {
        return coinz.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final  DrawableCoin drawableCoin = coinz.get(position);
        final Coin coin = drawableCoin.getCoin();

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(appContext);
            convertView = layoutInflater.inflate(R.layout.coin_grid_cell, null);
        }

        int imageResourceID = 0;

        switch (coin.type) {
            case PENY: imageResourceID = R.drawable.icon_peny_coin_large; break;
            case DOLR: imageResourceID = R.drawable.icon_dolr_coin_large; break;
            case QUID: imageResourceID = R.drawable.icon_quid_coin_large; break;
            case SHIL: imageResourceID = R.drawable.icon_shil_coin_large; break;
            default: break;
        }

        final ImageView imageView = convertView.findViewById(R.id.coin_grid_cell_coin_icon);
        final TextView textViewCoinValue = convertView.findViewById(R.id.textview_coin_value);
        final TextView textViewCoinType = convertView.findViewById(R.id.textview_coin_type);
        final LinearLayout linearLayout = convertView.findViewById(R.id.coin_layout);

        if (drawableCoin.isSelected) {
            linearLayout.setBackgroundResource(R.drawable.white_border);
        } else {
            linearLayout.setBackgroundResource(0);
        }

        imageView.setImageResource(imageResourceID);
        textViewCoinValue.setText(String.format(Locale.getDefault(), "%5f", coin.value));
        textViewCoinType.setText(String.format("%s", coin.type.toString()));

        return convertView;
    }
}
