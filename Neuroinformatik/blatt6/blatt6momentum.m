% Andra Herta
% Dimitrij Zharkov
% Tobias Dreher

function [  ] = blatt6momentum( )
   clc
   X = [0 0 0;0 0 1;0 1 0;0 1 1;1 0 0;1 0 1; 1 1 0; 1 1 1];
   T = [0 1 1 0 1 0 0 0]';
   syms x
   fun = tanh (x);
   tic
   MLP_on(X, T, 3, 2, 1, fun, 0.01, 10000, 0.5, 1, 0.9)
   toc
end

function [] = MLP_on (X, T, n, h, p, Fsym, lrate, lepoch, a, fen, momentum)
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

       deltaW2 = 0.0;
       deltaW1 = 0.0;
       deltaS2 = 0.0;
       deltaS1 = 0.0;
       
      for k = 1 : size (X, 1)   % Alle Muster durch
         % Vorwaertsphase
         U1 = X(k,:) * W1 - S1;
         Y1 = F(U1);

         U2 = Y1 * W2 - S2;
         Y2 = F(U2);

         % Fehler am Netzausgang
         D2 = (T(k,:) - Y2) .* f(U2);

         % Rueckwaertsphase
         D1 = D2 * W2' .* f(U1);
         
         %Lernen mit Momentumsterm
         deltaW2 = lrate * Y1' * D2 + momentum * deltaW2;
         W2 = W2 + deltaW2;
         deltaW1 = lrate * X(k,:)' * D1 + momentum * deltaW1;
         W1 = W1 + deltaW1;
         deltaS2 = lrate * D2 + momentum * deltaS2;
         S2 = S2 - deltaS2;
         deltaS1 = lrate * D1 + momentum * deltaS1;
         S1 = S1 - deltaS1;
      end

      E = 0;

      for k = 1 : size (X,1)   % Alle Muster durch
         % Vorwaertsphase
         U1 = X(k,:) * W1 - S1;
         Y1 = F(U1);
         
         U2 = Y1 * W2 - S2;
         Y2 = F(U2);
         
         % Fehler am Netzausgang ueber Muster aufsummieren
         E = E + sum((T(k,:) - Y2).^2); 
      end   
      % Gesamtfehler nach Iteration merken
      Err(i,1) = E;
   end

   figure(fen)
   plot(Err,'.')
   title(['Quadratischer Fehler mit ' num2str(h) ' Neuronen in der Zwischenschicht']);
   xlabel('Iteration')
   ylabel('Fehler')
   drawnow;
end
