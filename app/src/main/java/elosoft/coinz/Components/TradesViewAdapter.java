package elosoft.coinz.Components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;


import java.util.ArrayList;

import elosoft.coinz.Models.Trade;
import elosoft.coinz.R;

import static elosoft.coinz.Utility.User.CoinzUtility.calculateGoldFromCoinz;

public class TradesViewAdapter extends BaseAdapter {
    private final Context appContext;
    public final ArrayList<Trade> trades;
    private final String buttonText;
    private final String showButtonStatusText;

    public TradesViewAdapter(Context context, ArrayList<Trade> trades, String buttonText,
                             String showButtonStatusText) {
        this.appContext = context;
        this.trades = trades;
        this.buttonText = buttonText;
        this.showButtonStatusText = showButtonStatusText;
    }

    @Override
    public int getCount() {
        return trades.size();
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
        final  Trade trade = trades.get(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(appContext);
            convertView = layoutInflater.inflate(R.layout.trade_list_cell, null);
        }

        TextView partnerToUser = convertView.findViewById(R.id.partner_to_user_text);
        TextView userToPartner = convertView.findViewById(R.id.user_to_partner_text);
        TextView partnerToUserGold = convertView.findViewById(R.id.partner_to_user_gold);
        TextView userToPartnerGold = convertView.findViewById(R.id.user_to_partner_gold);
        TextView status = convertView.findViewById(R.id.status);
        Button listButton = convertView.findViewById(R.id.list_button);
        listButton.setText(this.buttonText);
        if (trade.getStatus().equals(this.showButtonStatusText)) {
            listButton.setVisibility(View.VISIBLE);
        }

        partnerToUser.setText(String.format("%s -> %s", trade.getPartner(), trade.getUser()));
        userToPartner.setText(String.format("%s -> %s", trade.getUser(), trade.getPartner()));
        status.setText(String.format("%s", trade.getStatus()));
        double partnerGold = calculateGoldFromCoinz(trade.getPartnerTradeCoinz(), trade.getExchangeRate());
        double userGold = calculateGoldFromCoinz(trade.getUserTradeCoinz(), trade.getExchangeRate());
        partnerToUserGold.setText(String.format("%.2f GOLD", userGold));
        userToPartnerGold.setText(String.format("%.2f GOLD", partnerGold));
        return convertView;
    }
}
