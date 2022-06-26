# Pulsar-Flink Stateful Streams

## Info

Watch the RockTheJVM video [here](https://www.youtube.com/watch?v=2ARIjulLE9M).

Read the blog entry [here](https://blog.rockthejvm.com/pulsar-flink/).

## Usage

1. Start the services via `docker-compose up`.
2. Run the `setup.sh` script to setup the topics.
3. Run `io.ipolyzos.producers.LookupDataProducer` to populate Pulsar with User and Item data. Close the program when
   it's done.
4. Modify and run the `deploy.sh` script. This will package and deploy the JAR to the task manager.
5. Go to localhost:8081 to get insight on what Flink is doing.
6. 