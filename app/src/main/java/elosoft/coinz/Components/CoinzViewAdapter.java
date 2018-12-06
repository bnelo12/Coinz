package elosoft.coinz.Components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import elosoft.coinz.Models.Coin;
import elosoft.coinz.R;

public class CoinzViewAdapter extends BaseAdapter {

    private final Context appContext;
    private final ArrayList<Coin> coinz;

    public CoinzViewAdapter(Context context, ArrayList<Coin> coinz) {
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
        final Coin coin = coinz.get(position);

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

        final ImageView imageView = (ImageView)convertView.findViewById(R.id.coin_grid_cell_coin_icon);
        final TextView textView = (TextView)convertView.findViewById(R.id.textview_coin_value);

        imageView.setImageResource(imageResourceID);
        textView.setText(String.format("%5f", coin.value));

        return convertView;
    }
}
