function [] = perceptron()
    % init values
    points = [-3,1; -3,3; -2,1; -2,4; -1,3; -1,4; 2,2; 2,4; 3,2; 4,1];
    labels = [0; 1; 0; 1; 1; 1; 0; 1; 0; 0];
    w = [1,0];
    theta = 0;
    leanringRate = 1; 
    generation = 0;
    errors = 1;
    
    % plot points
    plot(0)
    axis([-4.5,5.5,-1,5]);
    hold on
    for i=1:length(points)
        if labels(i) == 0
            plot(points(i,1),points(i,2),'s')
        else
            plot(points(i,1),points(i,2),'o')
        end
    end

    % algorithm
    while(errors ~= 0)
        errors = 0;

        for i=1:length(points)
            [w,theta,errors] = learning(w,theta,leanringRate,points(i,:),labels(i),errors);
        end;

        generation = generation + 1;

        x = -4:1:5;
        y = (theta-x*w(1))/w(2);   
        plot(x,y);
    end;

    fprintf('generations (without last iteration): %d\n',generation-1);

function [w,theta,fehler] = learning(w,theta,lernrate,x,label,fehler)
    u = dot(x,w) - theta;

    y=1;
    if u<0 
       y=0;
    end

    delta = label - y;
    
    if delta ~= 0
        fehler = fehler + 1;
        w = w + lernrate * delta * x;
        theta = theta - delta * lernrate;
    end       
