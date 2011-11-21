function [ ] = aufgabe6( )
%AUFGABE6 Optimale Clusterung mit k=2 und 2 < n < 6
% Die Ausgabe ist eine Matrix. Die Position in der Matrix gibt den/die Punkt/e
% der ersten Partion an. Die Partition besteht aus einem oder zwei Punkten. Der
% Wert an dieser Position gibt den Mittelwert der Durchmesser der einzelnen
% Partitionen an.
    P = [1 0;2 0;0 1;2 2;2 1];
    D = squareform(pdist(P)); % Distanzmatrix errechnen
    M = cluster_abstaende(D); % 2 Partitionen erstellen und Abstaende errechnen
    disp(M);
end

function M = cluster_abstaende(D)
    M = zeros(size(D,1),size(D,1));
    for i=1:size(D,1);
        for j=i:size(D,1);
            % Durchmesser von Partition 1 (i und j)
            dis1 = D(i,j);
            % Durchmesser von Partition 2 (alle Elemente ausser i und j)
            tmpD = D; % Distanzmatrix zwischenspeichern
            tmpD(1:size(D,1),i) = 0; % i und j verbindungen loeschen
            tmpD(1:size(D,1),j) = 0;
            tmpD(i,1:size(D,1)) = 0;
            tmpD(j,1:size(D,1)) = 0;
            dis2 = max(tmpD(:)); % groesster abstand
            % Durchschnitt errechnen und speichern
            M(i,j) = (dis1 + dis2)/2;
        end
    end
end