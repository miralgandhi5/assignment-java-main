package edu.knoldus;

import edu.knoldus.operation.TwitterOperation;

import java.time.LocalDate;

public class TwitterApplication {

    public static void main(String[] args) throws InterruptedException {

        TwitterOperation twitterOperation = TwitterOperation.getTwitterOps();
        final LocalDate DATE = LocalDate.of(2018, 2, 25);
        final Integer LIMIT = 100;

        System.out.println(" get latest to older tweets ");
        twitterOperation.newerToOlderPost(LIMIT)
                .thenAccept(optionalTweets -> optionalTweets
                        .ifPresent(tweets -> tweets.forEach(System.out::println)));
        Thread.sleep(5000);
        System.out.println(" get older to newer tweets ");
        twitterOperation.olderToNewerPost(LIMIT)
                .thenAccept(optionalTweets -> optionalTweets
                        .ifPresent(tweets -> tweets.forEach(System.out::println)));
        Thread.sleep(5000);
        System.out.println(" get tweets sorted by reTweets ");
        twitterOperation.postByReTweets()
                .thenAccept(optionalTweets -> optionalTweets
                        .ifPresent(tweets -> tweets.forEach(System.out::println)));
        Thread.sleep(5000);
        System.out.println(" get tweets sorted by likes ");
        twitterOperation.postByLikes()
                .thenAccept(optionalTweets -> optionalTweets
                        .ifPresent(tweets -> tweets.forEach(System.out::println)));
        Thread.sleep(5000);
        System.out.println(" get tweets sorted by date " + DATE);
        twitterOperation.postByDate(DATE)
                .thenAccept(optionalTweets -> optionalTweets
                        .ifPresent(tweets -> tweets.forEach(System.out::println)));
        Thread.sleep(1000);


    }
}
