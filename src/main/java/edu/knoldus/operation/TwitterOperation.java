package edu.knoldus.operation;


import edu.knoldus.utilities.TwitterConfig;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.QueryResult;
import twitter4j.Query;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


public class TwitterOperation {
    private static final TwitterOperation TWITTER_OPERATION = new TwitterOperation();
    private Twitter twitter;

    private TwitterOperation() {
        ConfigurationBuilder configBuilder = new ConfigurationBuilder();
        configBuilder.setDebugEnabled(true)
                .setOAuthConsumerKey(TwitterConfig.CONSUMER_KEY)
                .setOAuthConsumerSecret(TwitterConfig.CONSUMER_SECRET)
                .setOAuthAccessToken(TwitterConfig.ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(TwitterConfig.ACCESS_SECRET);
        TwitterFactory tweetFactory = new TwitterFactory(configBuilder.build());
        this.twitter = tweetFactory.getInstance();
    }

    public static TwitterOperation getTwitterOps() {
        return TWITTER_OPERATION;
    }

    /**
     * gives newest to oldest post from user timeline.
     *
     * @param limit specifies number of post to fetch.
     * @return optional of list of status.
     */
    public CompletableFuture<Optional<List<Status>>> newerToOlderPost(Integer limit) {
        return CompletableFuture.supplyAsync(() -> {

            List<Status> result = null;
            try {
                result = twitter.getHomeTimeline().stream().limit(limit).collect(Collectors.toList());
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return Optional.ofNullable(result);

        }).exceptionally(ex -> Optional.of(Collections.emptyList()));
    }

    /**
     * gives oldest to latest post from user timeline.
     * @param limit specifies number of post to fetch.
     * @return optional of list of status.
     */

    public CompletableFuture<Optional<List<Status>>> olderToNewerPost(Integer limit) {
        return newerToOlderPost(limit).thenApply(list -> {
            List<Status> statuses = list.orElse(Collections.emptyList());
            statuses.sort(Comparator.comparing(Status::getCreatedAt).reversed());
            return Optional.of(statuses);
        });

    }

    /**
     * gives list of post sorted by reTweet count.
     * @return optional of list of status.
     */

    public CompletableFuture<Optional<List<Status>>> postByReTweets() {
        return newerToOlderPost(100).thenApply(list -> {
            List<Status> statuses = list.orElse(Collections.emptyList());
            statuses.sort(Comparator.comparing(Status::getRetweetCount));
            return Optional.of(statuses);
        });

    }

    /**
     * gives list of post sorted by reTweet count.
     * @return optional of list of status.
     */

    public CompletableFuture<Optional<List<Status>>> postByLikes() {
        return newerToOlderPost(100).thenApply(list -> {
            List<Status> statuses = list.orElse(Collections.emptyList());
            statuses.sort(Comparator.comparing(Status::getFavoriteCount));
            return Optional.of(statuses);
        });

    }

    /**
     * gives list of status for the given date.
     * @param date it is date for which tweets are found.
     * @return optional of list of status.
     */

    public CompletableFuture<Optional<List<Status>>> postByDate(LocalDate date) {
        return CompletableFuture.supplyAsync(() -> {
            QueryResult result = null;
            try {
                Query query = new Query(twitter.getScreenName());
                query.setSince(date.toString());
                result = twitter.search(query);

            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return Optional.ofNullable(result != null ? result.getTweets() : null);

        }).exceptionally(ex -> Optional.of(Collections.emptyList()));

    }


}
