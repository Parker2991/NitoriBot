module.exports = {
  prefixes: ["!"],

  discord: {
    token: "discord token here",
    prefix: "!",
    invite: "discord invite here",
    enabled: true,
    presence: {
      name: "amogus",
      status: "dnd",
    },

    roles: {
      trusted: "trusted",
      admin: "admin",
      fullAccess: "full access",
      owner: "FNFBoyfriendBot Owner",
    },
  },

  core: {
    name: { text: "FNFBoyfriendBot Core", color: "#00FFFF" },
    itemName: { text: "FNFBoyfriendBot core item", color: "#00FFFF" },
    itemRefill: true,
    refillInterval: 300000,
    // refilling core via item is buggy rn i recommend setting itemRefill to false for now
    area: {
      start: {
        x: 0,
        y: 0,
        z: 0,
      },
      end: {
        x: 15,
        y: 0,
        z: 15,
      },
    },
  },

  weatherApiKey: "weather api token here",

  debug: {
    commandSetMessage: false,
  },

  keys: {
    trusted: "trusted key here",
    admin: "admin key here",
    owner: "owner key here",
  },

  colors: {
    integer: "gold",
    discord: {
      embed: "#000000",
      error: "#FF0000",
    },

    commands: {
      primary: "blue",
      secondary: "aqua",
      tertiary: "gray",
    },

    help: {
      public: "blue",
      trusted: "dark_aqua",
      admin: "aqua",
      owner: "light_purple",
      console: "dark_purple",
    },
  },

  console: {
    prefix: "c.",
  },

  logsFolder: {
    path: "../../../",
    // this is being loaded from the modules so make sure it starts with ../../
    name: "fnfboyfriendbot-logs",
  },

  bots: [
    // usernameGen is for a random username
    // if private is set to true then the server ip for the server will be hidden
    // channelId is for discord logging
    // logging is for console logging
    // reconnectDelay is for the delay for reconnecting to a server
    // modes:
    // kaboom: for the server kaboom.pw
    // savageFriends: for the server savage Friends
    // creayun for the server creayun
    {
      host: "localhost",
      username: "FNFBoyfriendBot",
      usernameGen: true,
      version: "1.20.2",
      serverName: "localhost",
      private: false,
      reconnectDelay: 6000,
      channelId: "discord channel ip here",
      logging: true,
      mode: "kaboom",
      selfcareInterval: 300,
    },
  ],
};
