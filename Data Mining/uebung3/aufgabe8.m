function [ ] = aufgabe8( )
%AUFGABE8 Clusterung mittels dem Austauschverfahren
    
    k=3; % geht aktuell nur mit k = 3, wegen function "init"
    n_min = 1;
    t_max = 500;
    dVarPlot = zeros(t_max,1);
    
    Data = load('80X.txt');
    C = init(Data);
    for t=1:t_max
        % Fuer Plot: D_var speichern
        dVarPlot(t) = fqsk(C);
    
        % Waehle zufaelligen Punkt aus
        [x,p,z] = zufaelligerPunkt(C, k);
        % Ist die Partition beinahe leer, waehle den naechsten Punkt
        if (size(C{p},1) <= n_min)
            min = size(C{p},1)
            continue;
        end
        % Transportiere Punkt, wenn sinnvoll
        for j=1:k
            if (j == p) % Punkt schon in Partition enthalten
                continue;
            end
            % Aktuelles Fehlerquaradtsummenkriterium errechnen
            D_var_cur = fqsk(C);
            
            % Versuchsweise Punkt x transportieren
            % Backup erzeugen
            C_p = C{p};
            C_j = C{j};
            % x von Partition p entfernen
            C{p}(z,:) = [];
            % x zu Partition j hinzufuegen
            C{j} = cat(1,C{j},x);
            
            % Neues Fehlerquaradtsummenkriterium errechnen
            D_var_new = fqsk(C);
            
            % Wenn D_var_new <= D_var_cur, dann belasse C so und passe
            % Zeilen- und Partitionszahl an
            if (D_var_new <= D_var_cur)
                p = j;
                z = size(C{j},1); % unterste Zeile
            else
                % Punkt zurueck transportieren
                C{p} = C_p;
                C{j} = C_j;
            end
        end
    end
    
    % plot 
    plot(dVarPlot)
end

% Partitionen zufaellig initialisieren
function C = init(Data)
    C1 = zeros(0,length(Data(1,:)));
    C2 = zeros(0,length(Data(1,:)));
    C3 = zeros(0,length(Data(1,:)));
    r = randi(3,1,length(Data(:,1)));
    for i=1:length(Data(:,1))
        switch r(i)
            case 1 
                C1 = [C1;Data(i,:)];
            case 2 
                C2 = [C2;Data(i,:)];
            otherwise
                C3 = [C3;Data(i,:)];
        end            
    end
    C = {C1,C2,C3};
end

% zufaelliger Punkt auswaehlen
% return: x = Punkt, p = Zahl der Partition, z = Zahl der Zeile in p
function [x,p,z] = zufaelligerPunkt(C, k)
    % Zufallszahl
    anzahl = 0;
    for i=1:k
        anzahl = anzahl + size(C{i},1);
    end
    r = randi(anzahl,1,1);
    
    % Punkt der Zufallszahl
    x = zeros(1,size(C{1},2));
    for i=1:k
        if (r <= size(C{i},1))
            c = C{i};
            x = c(r,:); % Punkt
            p = i; % Partition
            z = r; % Zeile
            break;
        else
            r = r - size(C{i},1);
        end
    end
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