% Andra Herta
% Dimitrij Zharkov
% Tobias Dreher

function [  ] = exercise16( )
iterations = 50;
cluster = 50;
learningRate = 0.05;
X = load('tree.pat');
tic
competitiveLearning(X, iterations, cluster, learningRate)
toc
end

function [] = competitiveLearning(X, iterations, cluster, learningRate)
% init prototyps
in = inline('(rand(x,y))*a','x','y','a');
C = in(cluster,size(X,2),120);

% train
for i = 1 : iterations    
    order = randperm(size(X,1));
    % for all vectors
    for k = order
        % winner detection
        winner = 1;
        for j = 1:cluster
        	if (sum((X(k,:) - C(j,:)).^2) < sum((X(k,:) - C(winner,:)).^2))
        		winner = j;
            end
        end
        % winner update
        C(winner,:) = C(winner,:) + learningRate * (X(k,:) - C(winner,:));
    end
end

figure()
plot(X(:,1),X(:,2),'b.')
hold on
plot(C(:,1),C(:,2),'g.')
title('Competitive Learning');
xlabel('x');
ylabel('y');
drawnow;
end
