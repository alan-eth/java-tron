function sleep(millis) {
  return new Promise(resolve => setTimeout(resolve, millis));
}

// const chalk = require('chalk');

function log(x) {
  // process.stdout.write(chalk.yellow(x));
}

module.exports = async function (secs) {
  secs = secs || 1;
  console.log(`Sleeping for ${secs} second${secs === 1 ? '' : 's'}...`);
  await sleep(1000 * (secs || 1));
  console.log(' Slept.\n');
};
