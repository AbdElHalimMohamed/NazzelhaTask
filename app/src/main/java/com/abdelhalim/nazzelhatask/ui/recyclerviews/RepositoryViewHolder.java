package com.abdelhalim.nazzelhatask.ui.recyclerviews;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.abdelhalim.nazzelhatask.Constants;
import com.abdelhalim.nazzelhatask.R;
import com.abdelhalim.nazzelhatask.data_modules.AbstractDataModule;

/**
 * Created by Abd El-Halim on 10/14/2015.
 */
public class RepositoryViewHolder extends AbstractViewHolder {

    private TextView repoTitle;
    private TextView repoDescription;
    private TextView repoOwnerName;
    private View forkFlagView;
    private String repoUrl;
    private String ownerUrl;

    public RepositoryViewHolder(View itemView) {
        super(itemView);
        forkFlagView = itemView.findViewById(R.id.fork_flag_view);
        repoTitle = (TextView) itemView.findViewById(R.id.repo_title_tv);
        repoDescription = (TextView) itemView.findViewById(R.id.repo_desc_tv);
        repoOwnerName = (TextView) itemView.findViewById(R.id.repo_owner_tv);
    }

    @Override
    protected void bindViewHolder(AbstractDataModule dataModule) {
        repoTitle.setText((String) dataModule.getModuleMap().get(Constants.REPO_NAME_KEY));
        repoDescription.setText((String) dataModule.getModuleMap().get(Constants.REPO_DESC_KEY));
        repoOwnerName.setText((String) dataModule.getModuleMap().get(Constants.REPO_OWNER_NAME_KEY));
        forkFlagView.setBackgroundColor(((Boolean) dataModule.getModuleMap().get(Constants.REPO_FORK_KEY)) ?
                forkFlagView.getResources().getColor(R.color.light_green) : forkFlagView.getResources().getColor(R.color.transparent));
        repoUrl = (String) dataModule.getModuleMap().get(Constants.REPO_HTML_URL);
        ownerUrl = (String) dataModule.getModuleMap().get(Constants.REPO_USER_HTML_URL);
    }

    @Override
    protected boolean handleLongPress(final View view) {
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_open_repo_owner_page, null);
        Dialog dialog = new AlertDialog.Builder(view.getContext()).setView(dialogView).create();
        dialog.show();
        Button repoUrlBtn = (Button) dialogView.findViewById(R.id.open_repo_btn);
        repoUrlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrlInBrowser(v.getContext(), repoUrl);
            }
        });
        Button ownerUrlBtn = (Button) dialogView.findViewById(R.id.open_owner_btn);
        ownerUrlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrlInBrowser(v.getContext(), ownerUrl);
            }
        });
        return true;
    }

    private void openUrlInBrowser(Context context, String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }
}
