package elosoft.coinz.Components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import elosoft.coinz.Models.Trade;
import elosoft.coinz.R;
import elosoft.coinz.Utility.LocalStorage.LocalStorageAPI;
import elosoft.coinz.Utility.Network.FireStoreAPI;

import static elosoft.coinz.Utility.User.CoinzUtility.calculateGoldFromCoinz;

public class TradesViewAdapterPending extends TradesViewAdapter {
    public TradesViewAdapterPending(Context context, ArrayList<Trade> trades) {
        super(context, trades, "reject", "pending");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Trade trade = trades.get(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(appContext);
            convertView = layoutInflater.inflate(R.layout.trade_list_cell, null);
        }

        TextView partnerToUser = convertView.findViewById(R.id.partner_to_user_text);
        TextView userToPartner = convertView.findViewById(R.id.user_to_partner_text);
        TextView partnerToUserGold = convertView.findViewById(R.id.partner_to_user_gold);
        TextView userToPartnerGold = convertView.findViewById(R.id.user_to_partner_gold);
        TextView status = convertView.findViewById(R.id.status);
        Button listButton = convertView.findViewById(R.id.list_button_1);
        listButton.setText("reject");
        Button listButton2 = convertView.findViewById(R.id.list_button_2);
        listButton2.setText("approve");
        if (trade.getStatus().equals(this.showButtonStatusText)) {
            listButton.setVisibility(View.VISIBLE);
            listButton2.setVisibility(View.VISIBLE);
        } else {
            listButton.setVisibility(View.GONE);
            listButton2.setVisibility(View.GONE);
        }

        partnerToUser.setText(String.format("%s -> %s", trade.getPartner(), trade.getUser()));
        userToPartner.setText(String.format("%s -> %s", trade.getUser(), trade.getPartner()));
        status.setText(String.format("%s", trade.getStatus()));
        double partnerGold = calculateGoldFromCoinz(trade.getPartnerTradeCoinz(), trade.getExchangeRate());
        double userGold = calculateGoldFromCoinz(trade.getUserTradeCoinz(), trade.getExchangeRate());
        partnerToUserGold.setText(String.format("%.2f GOLD", userGold));
        userToPartnerGold.setText(String.format("%.2f GOLD", partnerGold));
        listButton.setOnClickListener(v -> {
            status.setText("rejected");
            listButton.setVisibility(View.GONE);
            listButton2.setVisibility(View.GONE);
            reject(trade);
        });
        listButton2.setOnClickListener(v -> {
            status.setText("approved");
            listButton.setVisibility(View.GONE);
            listButton2.setVisibility(View.GONE);
            approve(trade, partnerGold);
        });
        return convertView;
    }

    void approve(Trade trade, double partnerGold) {
        trade.setStatus("approved");
        FireStoreAPI.getInstance().addTrade(trade);
        FireStoreAPI.getInstance().removePendingTrade(trade);
        FireStoreAPI.getInstance().removeUserDepositedCoinz(trade.getPartner(), trade.getPartnerTradeCoinz().values());
        LocalStorageAPI.updateUserGOLD(appContext, partnerGold);
    }

    void reject(Trade trade) {
        trade.setStatus("rejected");
        FireStoreAPI.getInstance().removePendingTrade(trade);
        FireStoreAPI.getInstance().removeSentTrade(trade);
        FireStoreAPI.getInstance().addUserCollectedCoinz(trade.getUser(), trade.getUserTradeCoinz().values());
    }
}
