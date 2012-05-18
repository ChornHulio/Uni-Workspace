function [ ] = aufgabe2( )
%AUFGABE2_AUFRUF Funktion um Aufgabe2 auszufuehren

    erg = zeros(41,1);
    x = zeros(41,1);
    u_t = zeros(5,1);
    
    for k = 0:40
        x_k = -2 + k / 10;
        x(k+1,1) = x_k;
        erg(k+1,1) = neuron(x_k,u_t,0);
    end
    plot(x,erg);
end

