package elosoft.coinz.Components;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;

import elosoft.coinz.Models.Coin;
import elosoft.coinz.R;

public class HighScoreListAdapter extends BaseAdapter {    private final Context appContext;
    public final HashMap<String, Object> scores;

    public HighScoreListAdapter(Context context, HashMap<String, Object> scores) {
        this.appContext = context;
        this.scores = scores;
    }

    @Override
    public int getCount() {
        return scores.size();
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
        final HashMap<String, Object> score = (HashMap<String, Object>)scores.get(String.format("%d", position+1));
        Log.d("Report:", score.toString());
        final String user = (String)score.get("user");
        final Long num = (Long)score.get("score");

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(appContext);
            convertView = layoutInflater.inflate(R.layout.list_score_cell, null);
        }

        TextView positionText = convertView.findViewById(R.id.position);
        TextView userNameText = convertView.findViewById(R.id.username);
        TextView scoreText = convertView.findViewById(R.id.score);

        positionText.setText(String.format("%d.", position+1));
        userNameText.setText(user);
        scoreText.setText(num.toString() + " GOLD");

        return convertView;
    }
}
