function [ ] = aufgabe5( )
%AUFGABE5 Summary of this function goes here
% Detailed explanation goes here
    X = [1 2 2;2 1 2;0 1 3;3 4 3;0 3 4;2 3 2];
    
    % Distanzen errechnen
    disp('Distanzmatrix');
    d = pdist(X).^2;
    disp(squareform(d));
    
    % Single-Linkage-Verfahren
    disp('Single-Linkage-Verfahren');
    S = linkage(d, 'single');
    disp(S);
    figure(1);
    dendrogram(S,0);
    
    % Complete-Linkage-Verfahren
    disp('Complete-Linkage-Verfahren');
    C = linkage(d, 'complete');
    disp(C);
    figure(2)
    dendrogram(C,0);
    
    % Group-Average-Verfahren
    disp('Group-Average-Verfahren');
    A = linkage(d, 'average');
    disp(A);
    figure(3);
    dendrogram(A,0);
    
    % Centroid-Verfahren
    disp('Centroid-Verfahren');
    CE = linkage(d, 'centroid');
    disp(CE);
    figure(4);
    dendrogram(CE,0);
    
    % Ward-Verfahren
    disp('Ward-Verfahren');
    W = linkage(d, 'ward');
    disp(W);
    figure(5);
    dendrogram(W,0);

end
