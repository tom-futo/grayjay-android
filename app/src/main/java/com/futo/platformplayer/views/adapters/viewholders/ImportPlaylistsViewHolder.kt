package com.futo.platformplayer.views.adapters.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.futo.platformplayer.R
import com.futo.platformplayer.api.media.models.playlists.IPlatformPlaylistDetails
import com.futo.platformplayer.constructs.Event1
import com.futo.platformplayer.models.Playlist
import com.futo.platformplayer.views.adapters.AnyAdapter
import com.futo.platformplayer.views.others.Checkbox

class ImportPlaylistsViewHolder(private val _viewGroup: ViewGroup) : AnyAdapter.AnyViewHolder<SelectablePlaylist>(
    LayoutInflater.from(_viewGroup.context).inflate(R.layout.list_import_playlist, _viewGroup, false)) {

    private val _checkbox: Checkbox;
    private val _imageThumbnail: ImageView;
    private val _textName: TextView;
    private val _textMetadata: TextView;
    private val _root: LinearLayout;
    private var _playlist: SelectablePlaylist? = null;

    val onSelectedChange = Event1<SelectablePlaylist>();

    init {
        _checkbox = _view.findViewById(R.id.checkbox);
        _imageThumbnail = _view.findViewById(R.id.image_thumbnail);
        _textName = _view.findViewById(R.id.text_name);
        _textMetadata = _view.findViewById(R.id.text_metadata);
        _root = _view.findViewById(R.id.root);

        _checkbox.onValueChanged.subscribe {
            _playlist?.selected = it;
            _playlist?.let { onSelectedChange.emit(it); };
        };

        _root.setOnClickListener {
            _checkbox.value = !_checkbox.value;
            _playlist?.selected = _checkbox.value;
            _playlist?.let { onSelectedChange.emit(it); };
        };
    }

    override fun bind(value: SelectablePlaylist) {
        _textName.text = value.playlist.name;
        if(value.playlist.videoCount >= 0) {
            _textMetadata.text = "${value.playlist.videoCount} " + _view.context.getString(R.string.videos);
            _textMetadata.visibility = View.VISIBLE;
        }
        else
            _textMetadata.visibility = View.GONE;
        _checkbox.value = value.selected;

        val thumbnail = value.playlist.thumbnail;
        if (thumbnail != null)
            Glide.with(_imageThumbnail)
                .load(thumbnail)
                .placeholder(R.drawable.placeholder_channel_thumbnail)
                .into(_imageThumbnail);
        else
            Glide.with(_imageThumbnail).clear(_imageThumbnail);

        _playlist = value;
    }
}

class SelectablePlaylist(
    val playlist: IPlatformPlaylistDetails,
    var selected: Boolean = false
) { }