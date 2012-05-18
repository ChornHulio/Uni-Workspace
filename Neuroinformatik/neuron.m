function [ erg ] = neuron( e_extern, u_t, t )
%AUFGABE2 Neuron mit positiver Selbstrueckkopplung
%   e_extern = externer Input; bleibt konstant
%   e_netz = andere Neuronen oder Rueckkopplung
%   u_t = Werte der fuenf Vorgaenger
%   t = aktueller Zeitschritt

    zeitkonstante = 1;

    if t >= (20*zeitkonstante)
        erg = u_t(5);
    else
        delta = zeitkonstante/10/zeitkonstante;
        u_neu = (1-delta) * u_t(5) + delta*(e_extern + 2*u_t(1));
        u_t(1) = [];
        u_t(5) = 1/(1+exp(-u_neu));
        erg = neuron(e_extern, u_t, t+zeitkonstante/10);
    end

end

