% Andra Herta
% Dimitrij Zharkov
% Tobias Dreher

function [  ] = blatt8aufgabe13( )
lernepochen = 500;
[peaksX,peaksY,peaksZ] = peaks(31);
X = [peaksX(:) peaksY(:)];
T = peaksZ(:);
syms x
%fun = 1 / (1 + exp(-x));
fun = tanh(x);
tic
MLP_on(X, T, 2, 100, 1, fun, 0.01, lernepochen, 0.5)
toc
end

function [] = MLP_on (X, T, n, h, p, Fsym, lrate, lepoch, a)
% Differenzieren der Transferfunktion
fsym = diff (Fsym);
F = matlabFunction(Fsym);
f = matlabFunction(fsym);

% Init der Gewichte und Schwelle und Fehler
in = inline('(2*rand(x,y)-1)*a','x','y','a');

W1 = in(n,h,a);
S1 = in(1,h,a);
W2 = in(h,p,a);
S2 = in(1,p,a);

Err = zeros(lepoch,1);

for i = 1 : lepoch
    
    reihenfolge = randperm(size(X,1));
    
    % Eine Trainingsepoche
    for k = reihenfolge   % Alle Muster in zufälliger Reihenfolge durch
        % Vorwaertsphase
        U1 = X(k,:) * W1 - S1;
        Y1 = F(U1);
        U2 = Y1 * W2 - S2;
        Y2 = U2; %F(U2);
        
        % Fehler am Netzausgang
        D2 = (T(k) - Y2); %.* f(U2);
        
        % Rueckwaertsphase
        D1 = D2 * W2'.* f(U1);
        
        %Lernen
        W2 = W2 + lrate * Y1' * D2;
        W1 = W1 + lrate * X(k,:)' * D1;        
        S2 = S2 - lrate * D2;
        S1 = S1 - lrate * D1;
    end
    
    % Fehler ueberpruefen
    E = 0;
    for k = 1 : size(X,1)   % Alle Muster durch
        % Vorwaertsphase
        U1 = X(k,:) * W1 - S1;
        Y1 = F(U1);
        U2 = Y1 * W2 - S2;
        Y2 = U2; %F(U2);
        
        % Fehler am Netzausgang ueber Muster aufsummieren
        E = E + sum((T(k) - Y2).^2);
    end
    % Gesamtfehler nach Iteration merken
    Err(i,1) = E;
    
    % Zwischenergebnis ausgeben
    fprintf('Lernepoche %d: %d\n', i, E);
end

figure()
% Orginalfunktion
subplot(2,2,1);
peaks(31);
title('Originale peaks(31)-Funktion');
% Approximation
subplot(2,2,2);
Z = zeros(31,31);
for k = 1:size(X,1)
    Z(k) = (F(X(k,:) * W1 - S1) * W2 - S2);
end
surf(Z)
title('Approximierte peaks(31)-Funktion');
% Fehlerverlauf
subplot(2,2,3);
plot(Err,'.')
title('Fehlerverlauf');
xlabel('Iteration')
ylabel('Fehler')
% Differenz
subplot(2,2,4);
Z = zeros(31,31);
for k = 1:size(X,1)
    app = (F(X(k,:) * W1 - S1) * W2 - S2);
    org = T(k);
    Z(k) = abs(app - org);
end
surf(Z)
title('Differenz der beiden Funktionen');
drawnow;
end
