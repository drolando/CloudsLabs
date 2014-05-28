if [ "$1" == "--group" ]
then
    java -classpath dist/ZookeeperLab.jar fr.eurecom.rs.clouds.ZookeeperLab.GroupMembTest
elif [ "$1" == "--watches" ]
then
    java -classpath dist/ZookeeperLab.jar fr.eurecom.rs.clouds.ZookeeperLab.watches.ConfigurationTest
else
    echo "use ./run.sh [--group | --watches]"
fi
