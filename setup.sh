echo "=== Creating partitioned topics ==="
docker exec -it pulsar bin/pulsar-admin topics create-partitioned-topic -p 1 persistent://public/default/orders
docker exec -it pulsar bin/pulsar-admin topics create-partitioned-topic -p 1 persistent://public/default/users
docker exec -it pulsar bin/pulsar-admin topics create-partitioned-topic -p 1 persistent://public/default/items

#docker exec -it pulsar bin/pulsar-admin topics create persistent://public/default/orders
#docker exec -it pulsar bin/pulsar-admin topics create persistent://public/default/users
#docker exec -it pulsar bin/pulsar-admin topics create persistent://public/default/items

echo "=== Listing topics ==="
docker exec -it pulsar bin/pulsar-admin topics list public/default

echo "=== Setting topic retention ==="
docker exec -it pulsar bin/pulsar-admin topics set-retention -s -1 -t -1 persistent://public/default/users
docker exec -it pulsar bin/pulsar-admin topics set-retention -s -1 -t -1 persistent://public/default/items

echo "=== Getting topic retention ==="
docker exec -it pulsar bin/pulsar-admin topics get-retention persistent://public/default/users
docker exec -it pulsar bin/pulsar-admin topics get-retention persistent://public/default/items
