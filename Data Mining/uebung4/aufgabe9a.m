function [ ] = aufgabe9a( )
%AUFGABE9A Fuzzy-k-means Algorithmus
% Kleiner zweidimensionaler Datensatz
    
    data = [3 3;2 2;2 0;1 0;0 1;2 3];
    
    options = [ 2 ; 100 ; 1e-5 ; 1 ];
    
    [center,U,objFcn] = fcm(data, 2, options);
    maxU = max(U);
    index1 = find(U(1, :) == maxU);
    index2 = find(U(2, :) == maxU);
    figure
    line(data(index1, 1), data(index1, 2), 'linestyle',...
    'none','marker', 'o','color','g');
    line(data(index2,1),data(index2,2),'linestyle',...
    'none','marker', 'x','color','r');
    hold on
    plot(center(1,1),center(1,2),'ko','markersize',15,'LineWidth',2)
    plot(center(2,1),center(2,2),'kx','markersize',15,'LineWidth',2)
end