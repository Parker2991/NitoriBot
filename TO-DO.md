Note: each time changes are made push them to the dev branch in the bots repo 
1. Rewrite the version argument of the info command
2. Fix arguments for discord
3. Try adding a argument limit for commands maybe
4. Use the nbt parser that chayapak wrote for the bot for 1.21.5 command block output
5. Completely remove tptoggle selfcare since it’s no longer used
- 6. Have the bot get output from the actual core instead of a command block it places
- 7. Add support for 1.21 and 1.21.1 while keeping support for 1.19.3 to 1.20.2
8. Add a server check
9. Add source.sendfeedback back maybe
10. Add a check to sendFeedback for discord to see if the message length is longer than discord’s max message limit to output it to a file and then reply with the file in discord
11. Add a embed function for discord maybe
12. Have the bot output errors to a seperate file to make debugging easier
- 13. Revert class changes for usernameGen, loadModules, between, and ansi since it wouldn’t make much sense to make them classes instead of functions (chayapaks suggestion)
- 14. Have the bot.js class extend the event emitter (chayapak suggestion)
15. Maybe parse cspy in the chat module and emit it as a packet maybe
16. For the owner check module have the bot send a message to the player username thats in the config if the 18. username name is online and if theres no reply back in discord within 10 seconds (10000 ms) to start deoping, muting, kicking, and changing their gamemode
