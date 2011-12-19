function [ ] = aufgabe9b( )
%AUFGABE9 Fuzzy-k-means Algorithmus
% groesserer, 7-dimensionaler Datensatz
    
    data = load('80X.txt');
    
    options = [ 2 ; 100 ; 1e-5 ; 1 ];
    
    [center,U,objFcn] = fcm(data, 2, options);
    
    figure
    plot(objFcn)
    title('Objective Function Values')
    xlabel('Iteration Count')
    ylabel('Objective Function Value')
end