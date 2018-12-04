# Project Enterprise & Mobile
Team:
> - Kenan Ekici
> - Simon Bogaerts
> - Sinasi Yilmaz
> - Pieter-Jan Kerfs

Voor het OLOD Enterprise & Mobile hebben wij de opdracht gekregen om de PXL-pas (studentenkaart/badge) te digitaliseren.
Hierbij moeten wij gebruik maken van de volgende technologieën: 
> - Xamarin
> - Universal Windows Platform
> - Java Spring Framework

## Front End 
### Xamarin.Forms
De minimale vereisten voor de Xamarin applicatie omvat de mogelijkheid om in te loggen op een multiplatform applicatie. Elk gebruiker is gekoppeld aan een PXL-pas dat hun identificeert. Via deze identificatie kan de gebruiker zijn aanwezigheid op verschillende op voorhand aangemaakte evenementen registreren. Voorbeelden van evenementen zijn seminaries, verplichte lessen of examens. Het registreren van een aanwezigheid gebeurt door het scannen van een QR-code die gelinkt is aan een evenement. Een toezichthouder kan op elk moment een lijst van evenementen opvragen en hiervan de aanwezigheden opnemen of bekijken. De toezichthouder kan op twee manieren aanwezigheden opnemen. Een eerste manier is om de participanten een unieke QR-code van een evenement te laten scannen. Een tweede manier is om rond gaan en de unieke QR-code van de participanten te scannen. Op deze manier kan de toezichthouder een overzicht verzamelen van de alle aanwezigen en dit wegschrijven naar een databank en eventueel raadplegen.

Verder willen wij ook enkele quality-of-life-enhancements aanbrengen in deze applicatie om het leven van een PXL-collega makkelijker te maken. Zoals het toevoegen van een kalender of lesrooster en het makkelijk bereikbaar maken van de verschillende PXL-faciliteiten zoals MijnPXL, EPOS, PXL-mail en MijnSLB via een browserview. Hierbij hoort ook het raadplegen van het PingPing saldo en een persoonlijk profiel. Een snelle en intuïtieve user interface is dus een must.  

### UWP
Deze applicatie is een dashboard dat het mogelijk maakt om de evenementen aan te maken en de aanwezigheden die erbij horen te beheren en analyseren. Via de analyse van de aanwezigheden kunnen er verschillende statistieken gegenereerd worden. Bij het aanmaken van een evenement via het dashboard kan de gebruiker de verplichte groepen toewijzen waarvan er verwacht wordt dat ze aanwezig zijn. Hiervoor kan de gebruiker een groep kiezen van andere gebruikers (studenten, lectoren,...) of zelf één maken voor dat specifieke event. Het aanmaken van de groep kan ook elk moment gebeuren via het dashboard. Gebruikers die niet tot een groep behoren van een evenement kunnen hun aanwezigheid ook registreren. Zo kan de toezichthouder een onderscheid maken wie zich heeft geregistreerd op zijn evenement. 

## Back End 
### Spring
De API zal de basis vormen van de back end voor alle applicaties. Deze API zal geschreven worden in Java door gebruik te maken van het Spring framework.
Hibernate zal gebruikt worden als ORM voor de datalaag. We zorgen dat de API geschreven wordt op een RESTful manier zodat er een gemakkelijke communicatie gebeurt tussen beide applicaties en de database. Tijdens de ontwikkeling van de applicatie zal er gebruik gemaakt worden van dummy data die wij gaan seeden in de database. 
