package com.sies.tejas.musicgram.utils;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.sies.tejas.musicgram.records.SongResponse;

import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;


public class KMeansClustering {

    public List<SongResponse.Song> getTopClusteredSongs(List<SongResponse.Song> allSongs, int numClusters) {
        // extract playCounts
        List<DoublePoint> points = new ArrayList<>();
        for (SongResponse.Song song : allSongs) {
            points.add(new DoublePoint(new double[]{song.playCount() != null ? song.playCount() : 0}));
        }

        // run clustering
        KMeansPlusPlusClusterer<DoublePoint> clusterer = new KMeansPlusPlusClusterer<>(numClusters);
        List<CentroidCluster<DoublePoint>> clusters = clusterer.cluster(points);

        // Find the cluster with the highest average play count
        CentroidCluster<DoublePoint> topCluster = clusters.stream()
                .max(Comparator.comparingDouble(c -> c.getPoints().stream()
                        .mapToDouble(p -> p.getPoint()[0])
                        .average().orElse(0)))
                .orElse(null);

        if (topCluster == null) return new ArrayList<>();

        // Match clustered points back to original songs
        List<SongResponse.Song> clusteredSongs = new ArrayList<>();
        for (DoublePoint p : topCluster.getPoints()) {
            for (SongResponse.Song song : allSongs) {
                double pc = song.playCount() != null ? song.playCount() : 0;
                if (pc == p.getPoint()[0]) {
                    clusteredSongs.add(song);
                    break;
                }
            }
        }

        return clusteredSongs; // list of similar/highly-played songs
    }

}


/*
public class KMeansClustering {
    // Group songs by type
    public Map<String, List<AlbumItem>> clusterSongsByType(List<AlbumItem> songs) {
        Map<String, List<AlbumItem>> clusters = new HashMap<>();

        for (AlbumItem song : songs) {
            String type = song.albumTitle().toLowerCase();
            clusters.putIfAbsent(type, new ArrayList<>());
            clusters.get(type).add(song);
        }

        return clusters;
    }

    // Recommend songs from the same type cluster
    public List<AlbumItem> recommendByType(AlbumItem likedSong, List<AlbumItem> allSongs) {
        Map<String, List<AlbumItem>> clusters = clusterSongsByType(allSongs);
        String type = likedSong.albumTitle().toLowerCase();
        List<AlbumItem> cluster = clusters.getOrDefault(type, new ArrayList<>());

        List<AlbumItem> recommendations = new ArrayList<>();
        for (AlbumItem song : cluster) {
            if (!song.albumTitle().equalsIgnoreCase(likedSong.albumTitle())) {
                recommendations.add(song);
            }
        }
        return recommendations;
    }
}*/
