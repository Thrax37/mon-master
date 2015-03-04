/*jshint node:true */
'use strict';
var fs = require('fs');

var dataFile = 'lancer.txt';

function getData(callback) {
  fs.readFile(dataFile, 'utf8', function(err, contents) {
    var data = {};

    if(!err) {
      data = JSON.parse(contents);
    }

    callback(data);
  });
}

function saveData(data, callback) {
  fs.writeFile(dataFile, JSON.stringify(data), callback);
}

function parseArgs(args){
  var gameState = {
    monster: {},
    hunters: [],
    otherHunters: []
  };

  var argArray = args.split(';');

  gameState.round = +argArray.shift();
  var myId = +argArray.shift();

  gameState.monster.attack = +argArray.shift();
  gameState.monster.defense = +argArray.shift();
  gameState.monster.hp = +argArray.shift();
  var monsterTargetId = +argArray.shift();
  gameState.monster.nextMove = argArray.shift();
  gameState.monster.guard = 0;
  gameState.monster.sharpness = 0;

  for(var index = 0; index < argArray.length; index++) {
    var hunterArgs = argArray[index].split('_');
    var hunterId = +hunterArgs.shift();
    var hunter = {};
    hunter.weapon = hunterArgs.shift();
    hunter.attack = +hunterArgs.shift();
    hunter.defense = +hunterArgs.shift();
    hunter.hp = +hunterArgs.shift();
    hunter.energy = +hunterArgs.shift();
    hunter.guard = +hunterArgs.shift();
    hunter.speed = +hunterArgs.shift();
    hunter.sharpness = +hunterArgs.shift();
    hunter.aggro = +hunterArgs.shift();
    hunter.potions = +hunterArgs.shift();
    hunter.rations = +hunterArgs.shift();
    hunter.whetstones = +hunterArgs.shift();
    hunter.combo = hunterArgs.shift().split('');

    gameState.hunters.push(hunter);

    if(myId === hunterId) {
      gameState.me = hunter;
    }
    else {
      gameState.otherHunters.push(hunter);
    }

    if(monsterTargetId === hunterId) {
      gameState.monster.target = hunter;
    }
  }

  return gameState;
}

function getAttackEnergy(hunter) {
  return Math.floor(100 / (hunter.speed + 2));
}

function getDamage(attacker, defender, useGuard) {
  var guard = useGuard ? defender.guard : 0;

  return attacker.attack * (10 + attacker.sharpness - defender.defense - guard);
}

function isAttackingMe(gameState) {
  var nextMove = gameState.monster.nextMove;
  return nextMove === 'S' || (gameState.monster.target === gameState.me && (nextMove === 'A' || nextMove === 'C'));
}

function isSafeToTaunt(gameState) {
  var maxAggro = Math.max.apply(null, gameState.otherHunters.map(function(hunter){return hunter.aggro;}));
  return gameState.me.aggro + 300 < maxAggro;
}

function getAction(gameState) {
  var damage = getDamage(gameState.me, gameState.monster);
  if(gameState.me.combo.length === 2) {
    damage *= 3;
  }

  if(damage >= gameState.monster.hp && !gameState.monster.isImmune && gameState.me.energy >= getAttackEnergy(gameState.me)) {
    return 'B';
  }

  var monsterDamage = getDamage(gameState.monster, gameState.me);
  var attackingMe = isAttackingMe(gameState);

  if(monsterDamage > gameState.me.hp) {
    if(attackingMe) {
      if(gameState.me.energy >= 30) {
        return 'D';
      }

      if(monsterDamage < getDamage(gameState.monster, gameState.me, true) && gameState.me.energy >= 15) {
        return 'G';
      }      
    }

    if(gameState.me.potions > 0) {
      return 'P';
    }

    if(gameState.me.rations > 0) {
      return 'R';
    }
  }

  if(attackingMe && gameState.me.energy >= 15) {
    return 'G';
  }

  if(gameState.me.sharpness <= 1 && gameState.me.whetstones > 0 && !attackingMe) {
    return 'S';
  }

  if(gameState.me.energy >= getAttackEnergy(gameState.me) * Math.min((3 - gameState.me.combo.length), 1) && (gameState.me.combo.length < 2 || !gameState.monster.isImmune)) {
    return 'B';
  }

  if(gameState.me.energy <= 20 && isSafeToTaunt(gameState)) {
    return 'T';
  }

  if(gameState.me.rations > 0 && gameState.me.hp <= 70 && gameState.me.energy <= 50 && !attackingMe) {
    return 'R';
  }

  return 'W';
}

function startGame() {
  console.log('LA');
}

if(process.argv.length <= 2) {
  startGame();
}
else {
  var gameState = parseArgs(process.argv[2]);
  getData(function(data) {
    if(data.monsterLastAttack === 'F' || data.monsterLastAttack === 'C') {
      gameState.monster.isImmune = true;
    }
    data.monsterLastAttack = gameState.monster.nextMove;

    saveData(data, function() {
      console.log(getAction(gameState));
    });
  });
}
/*jshint node:true */
'use strict';
var fs = require('fs');

var dataFile = 'lancer.txt';

function getData(callback) {
  fs.readFile(dataFile, 'utf8', function(err, contents) {
    var data = {};

    if(!err) {
      data = JSON.parse(contents);
    }

    callback(data);
  });
}

function saveData(data, callback) {
  fs.writeFile(dataFile, JSON.stringify(data), callback);
}

function parseArgs(args){
  var gameState = {
    monster: {},
    hunters: [],
    otherHunters: []
  };

  var argArray = args.split(';');

  gameState.round = +argArray.shift();
  var myId = +argArray.shift();

  gameState.monster.attack = +argArray.shift();
  gameState.monster.defense = +argArray.shift();
  gameState.monster.hp = +argArray.shift();
  var monsterTargetId = +argArray.shift();
  gameState.monster.nextMove = argArray.shift();
  gameState.monster.guard = 0;
  gameState.monster.sharpness = 0;

  for(var index = 0; index < argArray.length; index++) {
    var hunterArgs = argArray[index].split('_');
    var hunterId = +hunterArgs.shift();
    var hunter = {};
    hunter.weapon = hunterArgs.shift();
    hunter.attack = +hunterArgs.shift();
    hunter.defense = +hunterArgs.shift();
    hunter.hp = +hunterArgs.shift();
    hunter.energy = +hunterArgs.shift();
    hunter.guard = +hunterArgs.shift();
    hunter.speed = +hunterArgs.shift();
    hunter.sharpness = +hunterArgs.shift();
    hunter.aggro = +hunterArgs.shift();
    hunter.potions = +hunterArgs.shift();
    hunter.rations = +hunterArgs.shift();
    hunter.whetstones = +hunterArgs.shift();
    hunter.combo = hunterArgs.shift().split('');

    gameState.hunters.push(hunter);

    if(myId === hunterId) {
      gameState.me = hunter;
    }
    else {
      gameState.otherHunters.push(hunter);
    }

    if(monsterTargetId === hunterId) {
      gameState.monster.target = hunter;
    }
  }

  return gameState;
}

function getAttackEnergy(hunter) {
  return Math.floor(100 / (hunter.speed + 2));
}

function getDamage(attacker, defender, useGuard) {
  var guard = useGuard ? defender.guard : 0;

  return attacker.attack * (10 + attacker.sharpness - defender.defense - guard);
}

function isAttackingMe(gameState) {
  var nextMove = gameState.monster.nextMove;
  return nextMove === 'S' || (gameState.monster.target === gameState.me && (nextMove === 'A' || nextMove === 'C'));
}

function isSafeToTaunt(gameState) {
  var maxAggro = Math.max.apply(null, gameState.otherHunters.map(function(hunter){return hunter.aggro;}));
  return gameState.me.aggro + 300 < maxAggro;
}

function getAction(gameState) {
  var damage = getDamage(gameState.me, gameState.monster);
  if(gameState.me.combo.length === 2) {
    damage *= 3;
  }

  if(damage >= gameState.monster.hp && !gameState.monster.isImmune && gameState.me.energy >= getAttackEnergy(gameState.me)) {
    return 'B';
  }

  var monsterDamage = getDamage(gameState.monster, gameState.me);
  var attackingMe = isAttackingMe(gameState);

  if(monsterDamage > gameState.me.hp) {
    if(attackingMe) {
      if(gameState.me.energy >= 30) {
        return 'D';
      }

      if(monsterDamage < getDamage(gameState.monster, gameState.me, true) && gameState.me.energy >= 15) {
        return 'G';
      }      
    }

    if(gameState.me.potions > 0) {
      return 'P';
    }

    if(gameState.me.rations > 0) {
      return 'R';
    }
  }

  if(attackingMe && gameState.me.energy >= 15) {
    return 'G';
  }

  if(gameState.me.sharpness <= 1 && gameState.me.whetstones > 0 && !attackingMe) {
    return 'S';
  }

  if(gameState.me.energy >= getAttackEnergy(gameState.me) * Math.min((3 - gameState.me.combo.length), 1) && (gameState.me.combo.length < 2 || !gameState.monster.isImmune)) {
    return 'B';
  }

  if(gameState.me.energy <= 20 && isSafeToTaunt(gameState)) {
    return 'T';
  }

  if(gameState.me.rations > 0 && gameState.me.hp <= 70 && gameState.me.energy <= 50 && !attackingMe) {
    return 'R';
  }

  return 'W';
}

function startGame() {
  console.log('LA');
}

if(process.argv.length <= 2) {
  startGame();
}
else {
  var gameState = parseArgs(process.argv[2]);
  getData(function(data) {
    if(data.monsterLastAttack === 'F' || data.monsterLastAttack === 'C') {
      gameState.monster.isImmune = true;
    }
    data.monsterLastAttack = gameState.monster.nextMove;

    saveData(data, function() {
      console.log(getAction(gameState));
    });
  });
}