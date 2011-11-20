#########################################
###### Aufgabe 1
daten <- read.csv("daten1.csv")
tabelle <- table(daten)

png()
pie(tabelle, main="Kreisdiagramm")
barplot(tabelle, main="Balkendiagramm", xlab="Wert", ylab="Häufigkeit")

table(tabelle) #Haeufigkeitstabelle

#########################################
###### Aufgabe 2
names(which.max(daten) # Modalwert
median(daten[,1]) # Median
quantile(daten[,1],probs=0.2) #1/5-Quantil
mean(daten) #Arithmetischer Mittelwert
max(daten)-min(daten) # Spannweite
IQR(daten[,1]) # Interquartilbereich
var(daten) # Varianz
sqrt(var(daten)) # Varianz
boxplot(daten,horizontal=TRUE,col="pink",main="Boxplot") # Boxplot

#########################################
###### Aufgabe 3
daten2 <- read.csv("daten2.csv",header=TRUE)
cor(daten2[,1],daten2[,2])
