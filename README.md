[![alt text](https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png "CC BY-NC-SA 4.0")](https://creativecommons.org/licenses/by-nc-sa/4.0/)

# Aircraft
a Craftbukkit/Spigot server plugin allowing players to fly anything they build

# Ingame commands
The Aircraft plugin offers two commands: `/aircraft` and `/ac` the later simply being just a shortand for the first. Individual instructions are then specified by a subcommand:

- `/ac pilot` Creates a new aircraft. If you already had an aircraft it will be replaced by the new one.
- `/ac copilot <player>` Grants the pilot rights to a player. The player does not have to be an OP.
- `/ac unpilot <player>` Removes the pilot rights from a co-pilot.
- `/ac stop` Unloads the current aircraft (NOT removing its blocks).
- `/ac delete` Unloads the current aircraft and removes its blocks.
- `/ac destroy` alias of `/ac delete`
- `/ac noclip` Toggles collisions ON or OFF.
- `/ac ground` Moves the aircraft to the ground level.
- `/ac board [player]` Teleports you (or the specified player) aboard the aircraft.
- `/ac speed <speed>` Sets the speed of the aircraft. Use values from `1` to `10`.
- `/ac tp` Teleports the aircraft to you.
- `/ac help` Displays the list of available commands with descriptions.
- `/ac ?` alias of `/ac help`

Calling `/ac` without any subcommand is treated as the `/ac pilot` command.

# Configuration
The `config.yml` file only contains 2 constants for you to change:

- `control` ID of the item used to control the aircraft. Only when holding this item will the aircraft move.
- `max-size` Maximum number of blocks an aircraft can consist of - use this to limit possible high-CPU usage when flying with a huge aircraft. 
