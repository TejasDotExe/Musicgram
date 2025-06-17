package com.sies.tejas.musicgram.model;

public record SearchListItem(
        String id,
        String title,
        String subtitle,
        String coverImage,
        Type type
) {
    public static enum Type {
        SONG,
        ALBUM,
        PLAYLIST,
        ARTIST
    }
}
