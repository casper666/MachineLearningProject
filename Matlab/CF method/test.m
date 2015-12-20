function [ results ] = test(weightA, weightS, weightP, partialHistoryArtists, partialHistorySongs, fullHistoryArtists, fullHistorySongs,userProfiles, num_songs_recommended )
%TEST Summary of this function goes here
%OUTPUT (n*1)vector represents the accuracy for each user.
%   Detailed explanation goes here

n = size(partialHistoryArtists,1);

results = zeros(n,1);

for i = 1:n
    i
    y = fullHistorySongs;
    x = fullHistoryArtists;
    z = userProfiles;
    user_p = z(i,:);
    x(i,:) = [];
    y(i,:) = [];
    z(i,:) = [];
    recs = recommendations(weightA, weightS, weightP, partialHistoryArtists(i,:), partialHistorySongs(i,:),user_p,x, y,z, num_songs_recommended);
    results(i) = test_func(recs, fullHistorySongs(i,:), partialHistorySongs(i,:));
end

end

