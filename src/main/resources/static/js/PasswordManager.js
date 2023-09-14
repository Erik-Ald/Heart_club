
export class PasswordManager {

    constructor(password) {
        this.minPasswordLength = 8;
        this.password = password;
        this.baseScore = 0;
        this.score = 0;

        this.num = {};
        this.num.Excess = 0;
        this.num.Upper = 0;
        this.num.Numbers = 0;
        this.num.Symbols = 0;

        this.bonus = {};
        this.bonus.Combo = 0;
        this.bonus.FlatLower = 0;
        this.bonus.FlatNumber = 0;
        this.bonus.Excess = 3;
        this.bonus.Upper = 4;
        this.bonus.Numbers = 5;
        this.bonus.Symbols = 5;
    }


    checkPasswordStrength() {

        if(this.password.length >= this.minPasswordLength) {
            this.baseScore = 50;
        } else {
            this.baseScore = 0;
        }
        this.analyzePassword();
        this.calcComplexity()
        return this.score;
    }

    analyzePassword() {
        for (let i=0; i<this.password.length;i++) {
            if (this.password[i].match(/[A-Z]/g)) {this.num.Upper++;}
            if (this.password[i].match(/[0-9]/g)) {this.num.Numbers++;}
            if (this.password[i].match(/(.*[!,@,#,$,%,^,&,*,?,_,~])/)) {this.num.Symbols++;}
        }

        this.num.Excess = this.password.length - this.minPasswordLength;

        if (this.num.Upper && this.num.Numbers && this.num.Symbols) {
            this.bonus.Combo = 25;
        }

        else if ((this.num.Upper && this.num.Numbers)
            || (this.num.Upper && this.num.Symbols)
            || (this.num.Numbers && this.num.Symbols))
        {
            this.bonus.Combo = 15;
        }

        if (this.password.match(/^[\sa-z]+$/))
        {
            this.bonus.FlatLower = -15;
        }

        if (this.password.match(/^[\s0-9]+$/))
        {
            this.bonus.FlatNumber = -35;
        }
    }

    calcComplexity()
    {
        this.score = this.baseScore + (this.num.Excess*this.bonus.Excess) + (this.num.Upper*this.bonus.Upper)
            + (this.num.Numbers*this.bonus.Numbers) +  (this.num.Symbols*this.bonus.Symbols)
            + this.bonus.Combo + this.bonus.FlatLower + this.bonus.FlatNumber;
    }
}