package haui.android.taskmanager.views;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.List;
import haui.android.taskmanager.R;
import haui.android.taskmanager.models.Tag;

public class TagAdapter extends ArrayAdapter<Tag> {
    private Context context;
    private List<Tag> tagList;
    private int layoutResourceId;

    public TagAdapter(Context context, int layoutResourceId, List<Tag> tagList) {
        super(context, layoutResourceId, tagList);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.tagList = tagList;
    }

    @Override
    public int getCount() {
        return tagList.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.tagName = convertView.findViewById(R.id.txtTagName);
            holder.colorTag = convertView.findViewById(R.id.colorTag);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Tag tag = getItem(position);
        if (tag != null) {
            holder.tagName.setText(tag.getTagName());
            try {
                String tagColor = tag.getTagColor();
                if (tagColor != null && !tagColor.isEmpty()) {
                    holder.colorTag.setBackgroundColor(Color.parseColor(tagColor));
                } else {
                    holder.colorTag.setBackgroundColor(Color.TRANSPARENT); // Hoặc một màu mặc định khác
                }
            } catch (IllegalArgumentException e) {
                // Xử lý trường hợp màu không hợp lệ
                holder.colorTag.setBackgroundColor(Color.TRANSPARENT); // Hoặc một màu mặc định khác
            }
        } else {
            // Xử lý trường hợp tag bị null
            holder.tagName.setText("");
            holder.colorTag.setBackgroundColor(Color.TRANSPARENT); // Hoặc một màu mặc định khác
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    static class ViewHolder {
        TextView tagName;
        RelativeLayout colorTag;
    }
}
