function [ userSimilarity ] = simUser(newUser, userHistory)
%SIMUSER return the similarities between users
%   INPUT:  1xm vector newUser
%           nxm matrix userHistory
%
%   OUTPUT: 1xm vector userSimilarity
%
%   returns the similarities between newUser and all the users in
% userHistory, based on the cosine of their vectors

[n,~] = size(userHistory);

userSimilarity = zeros(n,1);

for i = 1:n
    
    userSimilarity(i) = dot(userHistory(i,:), newUser)/(norm(userHistory(i,:),2)*norm(newUser,2));

end

end

