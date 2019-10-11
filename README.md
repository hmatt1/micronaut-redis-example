# micronaut-redis-example

## Background

This repo is set up to demonstate an issue where a blocking command is being run from a Netty Event Loop Group thread with Micronaut Redis.

My understanding is that threads named like `nioEventLoopGroup` should not have any blocking commands run on them.

## Steps to reproduce

1. Set a breakpoint in the `getCommands` method in `io.lettuce.core.dynamic.RedisCommandFactory`.

```
    private List<CommandDetail> getCommands(StatefulConnection<?, ?> connection) {

        List<Object> commands = Collections.emptyList();
        try {
            if (connection instanceof StatefulRedisConnection) {
                commands = ((StatefulRedisConnection) connection).sync().command();
            }

            if (connection instanceof StatefulRedisClusterConnection) {
                commands = ((StatefulRedisClusterConnection) connection).sync().command();
            }
        } catch (RedisCommandExecutionException e) {
            log.debug("Cannot obtain command metadata", e);
        }

        if (commands.isEmpty()) {
            setVerifyCommandMethods(false);
        }

        return CommandDetailParser.parse(commands);
    }
```


2. Run the `example.micronaut.HelloTest` in Intellij with debug.

3. When the breakpoint is hit, check out the stack trace and thread name. 

The thread name is `nioEventLoopGroup-1-3`.

Stack trace looks like this:

```
getCommands:132, RedisCommandFactory (io.lettuce.core.dynamic)
<init>:124, RedisCommandFactory (io.lettuce.core.dynamic)
<init>:108, RedisCommandFactory (io.lettuce.core.dynamic)
syncCommands:226, RedisCache (io.micronaut.configuration.lettuce.cache)
<init>:86, RedisCache (io.micronaut.configuration.lettuce.cache)
build:-1, $RedisCacheDefinition (io.micronaut.configuration.lettuce.cache)
build:113, BeanDefinitionDelegate (io.micronaut.context)
doCreateBean:1610, DefaultBeanContext (io.micronaut.context)
createAndRegisterSingleton:2319, DefaultBeanContext (io.micronaut.context)
getBeanForDefinition:2001, DefaultBeanContext (io.micronaut.context)
```
