function [ acuRate ] = test_func(recommd_rslt,test_data_mat,train_data)
%TEST Summary of this function goes here
%   recommd_rslt=c*1 mat
%   test_data_mat= 1 * n mat of test data of the selected user
%   acuRate= accurate 
m=size(test_data_mat);
t=test_data_mat;
for j=1:m
    if train_data(j)>0
        t(j)=0;
    end
end

sort(t,'descend');
n=size(recommd_rslt,1);
p=t(n+1);
sum=0;
for i=1:n
    if test_data_mat(recommd_rslt(i))>p
        sum=sum+1;
    end
end
acuRate=sum/n;
end

