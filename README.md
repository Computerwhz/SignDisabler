# SignDisabler

This is a simple Spigot plugin which helps you disable sign creation, editing, removal on a Minecraft server. It can be targeted at individual players or all players and can be easily be managed live with a simple command.

## Operation
**Note:** plugin is disabled by default you will have to run `/signdisabler enable` for the plugin to start functioning.

**Bypass permission:** `signdisabler.bypass` (The permission is the highest priority and will override all other settings) 

You may enable global mode which will disable signs for all players  (except those with the aforementioned bypass permission). The player setting will not be used when this is enabled otherwise it is the default.

## Command
The `/signdisabler` command takes up to 3 args

**Permission:** `signdisabler.admin`

| Command       | Arg1          | Arg2   | Arg3       | Description                                         |
|---------------|---------------|--------|------------|-----------------------------------------------------|
| /signdisabler | enable        |        |            | Resume plugin operation                             |
| /signdisabler | disable       |        |            | Pause plugin operation                              |
| /signdisabler | globaldisable | true   |            | Disable signs for all players                       |
| /signdisabler | globaldisable | false  |            | Only disable sign for players added to disable list |
| /signdisabler | player        | add    | <Username> |                                                     |
| /signdisabler | player        | list   |            |                                                     |
| /signdisabler | player        | remove | <Username> |                                                     |

