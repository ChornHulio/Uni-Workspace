function [ ] = aufgabe7( )
%AUFGABE7 Unterstützung bei der haendischen Rechnung
% Errechnet das Fehlerquadratsummenkriterium fuer ein Cluster mit zwei
% Partitionen
    c1 = [3 3;2 2];
    c2 = [2 0;1 0;0 1;2 3];    
    fqsk({c1,c2})
end

function D = fqsk(C)
    D = 0;
    for j=1:length(C)
        c_j=schwerpunkt(C{j});
        for m=1:length(C{j}(:,1))
            for i=1:length(C{j}(1,:))
                D = D + (C{j}(m,i) - c_j(i))^2;
            end
        end
    end
end

function c_j = schwerpunkt(C_j)
    c_j = zeros(length(C_j(1,:)),1);
    for i=1:length(C_j(1,:))
        for m=1:length(C_j(:,1))        
            c_j(i) = c_j(i) + C_j(m,i);
        end
        c_j(i) = c_j(i) / length(C_j(:,1));
    end
end