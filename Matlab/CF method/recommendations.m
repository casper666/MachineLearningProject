function [ recs ] = recommendations(weightA, weightS, weightP, userArtists, userSongs, userProfile, userArtistHistories, userSongHistories, userProfiles, c )
%RECOMMENDATIONS Predict how much user would like unheard songs
%   INPUT:  1xm vector userArtists
%           1xm vector userSongs
%           nxm matrix userArtistHistories
%           nxm matrix userSongHistories
%           1x1 scalar c
%
%   OUTPUT: rx1 vector recs
%
%   Given the history of the user we want to recommend to
% and the histories of all other users, assign prediction 
% values to all songs not in the user's history, and output the
% top c songs.

% listening histories for a song are normalized as the percent of the
% user's total listening history

[n,m] = size(userSongHistories);

simsA = simUser(userArtists,userArtistHistories);
simsS = simUser(userSongs,userSongHistories);
simsP = simUser(userProfile,userProfiles);
simsT = weightA .* simsA + weightS .* simsS + weightP .* simsP;

sums = sum(userSongHistories,2);

recValues = zeros(m, 1);
for i = 1:m
    r = 0;
    if userSongs(i)==0
        for j = 1:n
            if userSongHistories(j,i)~=0
               r = r + simsT(j) * (userSongHistories(j,i) / sums(j));
            end   
        end
    end
    recValues(i) = r;
end

recs = zeros(c,1);
for i = 1:c
    [~, l] = max(recValues);
    recs(i) = l;
    recValues(l) = 0;
end
end

