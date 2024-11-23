#UUIDsync

A Minecraft Plugin for Player Data Synchronization/一款用于玩家数据同步的 Minecraft 插件

UUIDSync is a Minecraft Bukkit plugin that allows two players with different UUIDs to share the same game data. 通过将两个 UUID 映射在一起，当其中一个玩家退出时，游戏数据会自动同步到另一个 UUID。It supports both online-mode and offline-mode servers, ensuring that the two bound players cannot be online at the same time. 它还支持 Folia 多线程架构，适用于大规模服务器。

Reminder/提醒: Two bound UUIDs are not allowed to go online at the same time to ensure data consistency./禁止两个绑定的 UUID 同时上线，确保数据一致性。

Features/功能

UUID Mapping: Use the command /cop <UUID1> <UUID2> 将两个 UUID 映射在一起。

Data Synchronization: 当映射的其中一个玩家退出游戏时，其数据会自动复制到另一个 UUID。

Persistent Mapping: The UUID mapping relationship is stored in the plugin's configuration file and is automatically loaded after a server restart/UUID 映射关系存储在插件配置文件中，服务器重启后会自动加载。

Full-Mode Compatibility: 支持在线模式、离线模式及 Folia 多线程架构的 Minecraft 服务器。


Use Cases/使用场景

Share game progress between two accounts on the same server/在同一服务器上共享两个账户的游戏进度。

Quickly switch between characters in a test environment while keeping consistent game data/在测试环境下快速切换角色，但保持一致的游戏数据。

Implement multi-account shared storage functionality in a server/在服务器中实现多账户共享存档的功能。

Exchange Java player data with bedrock player data in the interworking server./在互通服务器中把Java玩家数据和基岩玩家数据互通

Installation and Usage/安装与使用

1. Place the plugin .jar file into the plugins folder of your server/将插件 .jar 文件放入服务器的 plugins 文件夹中。


2. Start the server to generate the plugin files/启动服务器，生成插件文件。



