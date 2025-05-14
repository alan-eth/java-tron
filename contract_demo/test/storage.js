const StorageContract = artifacts.require('./Storage.sol');
const wait = require('./helpers/wait');

contract('Storage', function (accounts) {
    it('xxx', async function () {
        const st = await StorageContract.deployed();
        const a = 20250513;
        const b = a + 1;
        const c = b + 1;
        // try {

        const curNumber = await st.retrieve.call()
        console.log(`curNumber: ${curNumber.toString()}`)
        const res = await st.store(a, {
            from: accounts[0]
        })

        // console.log(`res is ${res}`);
        // await wait(3);
        // st.store(b, {
        //     from: accounts[0]
        // }).then(() => {
        //     console.log(`store ${b} success`)
        // }).catch((err) => {
        //     console.log(err)
        // });
        // await wait(3);
        // st.store(c, {
        //     from: accounts[0]
        // }).then(() => {
        //     console.log(`store ${c} success`)
        // }).catch((err) => {
        //     console.log(err)
        // });
        // await wait(3);
    });
});