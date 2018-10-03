# Tweelytics

Application which fetches real-time tweets based on keywords and geolocation determined by the end user and performs sentiment analysis on them by using the [Indico API](https://indico.io/docs) to predict:
* Audience engagement
* Political leaning
* Emotions

Overall statistics are also calculated to show the average prediction values on all tweets analyzed in the end user session.

Please, find an screenshot below to see how the output for an analyzed tweet would look like:

![analyzed tweet](example_ss.png)

## Software

Infrastructure-wise, this is the diagram which illustrates the distribution of the nodes and the applications deployed within them:

![component diagram](deploy_view.png)


Regarding the development of the application, the following diagram describes its structure in terms of software components:

![component diagram](component_diagram.png)
