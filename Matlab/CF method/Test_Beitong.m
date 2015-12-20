% ML_Proj.
% Test Script
% Feb. 14, 2015

load('data5000.mat');

art_Name_table=importdata('artist_list.txt','\n');
song_Name_table=importdata('song_list.txt','\n');
%song_train=load('user_song_vector_train.txt');
%song_test=load('user_song_vector_test.txt');
%art_train=load('user_artist_vector_train.txt');
%art_test=load('user_artist_vector_test.txt');
[song_row,song_col]=size(song_train);
[art_row,art_col]=size(art_train);
%Matrices loaded

%pick the first user
%aim_user_art=art_train(1,:);
%song_train_sub=song_test(2:song_row,:);
%art_train_sub=art_test(2:art_row,:);


%aim_user_recm=recommendations(aim_user_art,aim_user_songs,art_train_sub,song_train_sub,5)
%accu=test_func(aim_user_recm,art_test)

user_profiles = userprofilevector;

num_songs_recommended = 30;

weight_artists = 1;
weight_songs = 1;
weight_profile =0;

accu=mean(test(weight_artists,weight_songs,weight_profile,art_train,song_train,art_test,song_test,user_profiles, num_songs_recommended))


