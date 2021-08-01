DrumPattern {
	classvar <all;
	// var <>kick, <>sn, <>ch, <>oh, <>rim, <>cym, <>bell, <>cl, <>sh, <>ht, <>mt, <>lt, <>acc;
    var <>pattern;

	// set patterns with arrays of hit positions [1,3] == [1, \r, 4, \r]
	*new { arg
		kick=[],
		sn=[],
		sne=[],
		ch=[],
		ph=[],
		oh=[],
		soh=[],
		rim=[],
		cym=[],
		cym1=[],
		cym1ck=[],
		cym2=[],
		cym2ck=[],
		cym3=[],
		bell=[],
		rs=[],
		rt=[],
		rck=[],
		rbl=[],
		spl=[],
		cl=[],
		tb=[],
		sh=[],
		ht=[],
		hte=[],
		mt=[],
		lt=[],
		lte=[],
		acc=[],
		size=16;
		var pat = (
			kick: DrumPattern.asArray(kick, size),
			sn: DrumPattern.asArray(sn, size),
			sne: DrumPattern.asArray(sne, size), // snare edge
			ch: DrumPattern.asArray(ch, size),
			ph: DrumPattern.asArray(ph, size), // pedal hihat
			oh: DrumPattern.asArray(oh, size),
			soh: DrumPattern.asArray(soh, size), // semi-open hihat
			rim: DrumPattern.asArray(rim, size),
			cym: DrumPattern.asArray(cym, size),
			cym1: DrumPattern.asArray(cym1, size),
			cym1ck: DrumPattern.asArray(cym1, size), // cymbal 1 choked
			cym2: DrumPattern.asArray(cym2, size),
			cym2ck: DrumPattern.asArray(cym2, size), // cymbal 2 choked
			cym3: DrumPattern.asArray(cym3, size),
			bell: DrumPattern.asArray(bell, size),
			rs: DrumPattern.asArray(rs, size), // ride cymbal shank
			rt: DrumPattern.asArray(rt, size), // ride cymbal tip
			rck: DrumPattern.asArray(rck, size), // ride cymbal choked
			rbl: DrumPattern.asArray(rbl, size), // ride cymbal bell
			spl: DrumPattern.asArray(spl, size), // splach cym
			cl: DrumPattern.asArray(cl, size), // clap
			tb: DrumPattern.asArray(tb, size), // tambourine
			sh: DrumPattern.asArray(sh, size), // shaker - maraca
			ht: DrumPattern.asArray(ht, size), // high tom
			hte: DrumPattern.asArray(hte, size), // high tom edge
			mt: DrumPattern.asArray(mt, size), // mid tom
			lt: DrumPattern.asArray(lt, size), // low tom
			lte: DrumPattern.asArray(lte, size), // low tom edge
			acc: DrumPattern.asArray(acc, size); // accent
		);
		^super.newCopyArgs(pat);
    }

	// set patterns as arrays of 1's and \r's
    *with { arg
		kick=[],
		sn=[],
		sne=[],
		ch=[],
		ph=[],
		oh=[],
		soh=[],
		rim=[],
		cym=[],
		cym1=[],
		cym1ck=[],
		cym2=[],
		cym2ck=[],
		cym3=[],
		bell=[],
		rs=[],
		rt=[],
		rck=[],
		rbl=[],
		spl=[],
		cl=[],
		tb=[],
		sh=[],
		ht=[],
		hte=[],
		mt=[],
		lt=[],
		lte=[],
		acc=[];
		var pat = (
			kick: kick,
			sn: sn,
			sne: sne,
			ch: ch,
			ph: ph,
			oh: oh,
			soh: soh,
			rim: rim,
			cym: cym,
			cym1: cym1,
			cym1ck: cym1ck,
			cym2: cym2,
			cym2ck: cym2ck,
			cym3: cym3,
			bell: bell,
			rs: rs,
			rt: rt,
			rck: rck,
			rbl: rbl,
			spl: spl,
			cl: cl,
			tb: tb,
			sh: sh,
			ht: ht,
			hte: hte,
			mt: mt,
			lt: lt,
			lte: lte,
			acc: acc,
		);
		^super.newCopyArgs(pat);
    }

	*at { |key|
		^all.at(key)
	}

	*newFromKey { |key|
		var pattern = this.at(key).deepCopy;
		pattern ?? { ("Unknown pattern " ++ key.asString).warn; ^nil };
		^pattern;
	}

	*doesNotUnderstand { |selector, args|
		var pattern = this.newFromKey(selector, args).deepCopy;
		^pattern ?? { super.doesNotUnderstand(selector, args) };
	}

	*names {
		^all.keys.asArray.sort;
	}

	*directory {
		^this.names.collect(_.postln);
	}

	// returns array of given 'size' with default
	// values set to 'defaultValue', and values at
	// 'postitions' set to 'replaceValue'
    *asArray { arg positions, size=16, zeroIndex=false, defaultValue=\r, replaceValue=1;
        var arr = Array.fill(size, defaultValue);
		// zeroIndex.debug("zero");
        positions.do{|x|
			arr.put(x.asInt - zeroIndex.not.asInteger, replaceValue);
			// x.postln;
        }
        ^arr;
    }

	// load patterns into ~variables with the same name
	// TODO: if no pattern list is provided, all the patterns are loaded
	*asCurrentEnvironmentVariables {
		DrumPattern.directory.do{|k| currentEnvironment[k] = DrumPattern.all[k]};
	}

	// converts an array of 1's and \r's to an array of \durs
	*asDurs {  arg pat;
		var durs = List();
		var positions = pat.asString.replace(",", "").replace("[","").replace("]","").replace(" ","").findAll("1");
		positions.do{|x,i|
			if(i < (positions.size - 1)) {
				durs.add(positions[i + 1] - x);
			} {
				durs.add(pat.size - x);
			}
		};
		pat.debug("pattern");
		durs.debug("durs");
	}

	*fromString { arg str, kick="_", sn=".", ch="x", oh="o", rim="r", cym="z", bell="b", cl="c", sh="h", ht="t", mt="m", lt="l", rest=" ", acc="[A-Z]";
		var drumpat = DrumPattern(
			kick: str.toLower.findAllRegexp(kick) + 1,
			sn: str.toLower.findAllRegexp(sn) + 1,
			ch: str.toLower.findAllRegexp(ch) + 1,
			oh: str.toLower.findAllRegexp(oh) + 1,
			rim: str.toLower.findAllRegexp(rim) + 1,
			cym: str.toLower.findAllRegexp(cym) + 1,
			bell: str.toLower.findAllRegexp(bell) + 1,
			cl: str.toLower.findAllRegexp(cl) + 1,
			sh: str.toLower.findAllRegexp(sh) + 1,
			ht: str.toLower.findAllRegexp(ht) + 1,
			mt: str.toLower.findAllRegexp(mt) + 1,
			lt: str.toLower.findAllRegexp(lt) + 1,
			acc: str.toLower.findAllRegexp(acc) + 1,
			size: str.size,
		);

		// drumpat.print;
		^drumpat;
	}

	postln {
		this.pattern.keysValuesDo{|k, v| v.debug(k)};
	}

	newFromKey { |key|
		var pattern = this.pattern.at(key).deepCopy;
		pattern ?? { ("Unknown pattern " ++ key.asString).warn; ^nil };
		^pattern;
	}

	doesNotUnderstand { |selector, args|
		var pattern = this.newFromKey(selector, args).deepCopy;
		^pattern ?? { super.doesNotUnderstand(selector, args) };
	}

	at { arg key;
		^pattern.at(key);
	}

	fromString { arg str, kick="_", sn=".", ch="x", oh="o", rim="r", cym="z", bell="b", cl="c", sh="h", ht="t", mt="m", lt="l", rest=" ", acc="[A-Z]";
		this.pattern.kick = DrumPattern.asArray(str.toLower.findAllRegexp(kick) + 1, size: str.size);
		this.pattern.sn = DrumPattern.asArray(str.toLower.findAllRegexp(sn) + 1, size: str.size);
		this.pattern.ch = DrumPattern.asArray(str.toLower.findAllRegexp(ch) + 1, size: str.size);
		this.pattern.oh = DrumPattern.asArray(str.toLower.findAllRegexp(oh) + 1, size: str.size);
		this.pattern.rim = DrumPattern.asArray(str.toLower.findAllRegexp(rim) + 1, size: str.size);
		this.pattern.cym = DrumPattern.asArray(str.toLower.findAllRegexp(cym) + 1, size: str.size);
		this.pattern.bell = DrumPattern.asArray(str.toLower.findAllRegexp(bell) + 1, size: str.size);
		this.pattern.cl = DrumPattern.asArray(str.toLower.findAllRegexp(cl) + 1, size: str.size);
		this.pattern.sh = DrumPattern.asArray(str.toLower.findAllRegexp(sh) + 1, size: str.size);
		this.pattern.ht = DrumPattern.asArray(str.toLower.findAllRegexp(ht) + 1, size: str.size);
		this.pattern.mt = DrumPattern.asArray(str.toLower.findAllRegexp(mt) + 1, size: str.size);
		this.pattern.lt = DrumPattern.asArray(str.toLower.findAllRegexp(lt) + 1, size: str.size);
		this.pattern.acc = DrumPattern.asArray(str.toLower.findAllRegexp(oh) + 1, size: str.size);
	}

// the patterns

	*initClass {
		all = IdentityDictionary[
            // basic
        \basic -> DrumPattern(
            kick: [1,7],
            sn: [5,13],
        ),
        \boots -> DrumPattern(
            kick: [1,9],
            sn: [5,13],
            ch: [1,3,5,7,9,11,13,15],
        ),
        \tinyhouse -> DrumPattern(
            kick: [1,5,9,13],
            oh: [3,7,11,15],
        ),
        \goodtogo -> DrumPattern(
            kick: [1,4,7,11],
            sn: [5,13],
        ),
        \hiphop -> DrumPattern(
            kick: [1,3,7,8,15],
            sn: [5,13],
            ch: [1,3,5,7,9,11,13,15],
        ),
        // standard breaks
        \stdbreak1 -> DrumPattern(
            kick: [1,11],
            sn: [5,13],
            ch: [1,3,5,7,9,10,11,13,15],
        ),
        \stdbreak2 -> DrumPattern(
            kick: [1,11],
            sn: [5,13],
            ch: [1,3,5,7,8,9,11,15],
        ),
        \rollingbreak -> DrumPattern(
            kick: [1,8,11],
            sn: [5,13],
            ch: [1,3,5,7,9,11,15],
        ),
        \unknowndrummer -> DrumPattern(
            acc: [5,13],
            kick: [1,8,11],
            sn: [5,13],
            ch: [1,3,5,7,9,11,15],
        ),
        // rock
        \rock1 -> DrumPattern(
            kick: [1,8,9,11],
            sn: [5,13],
            ch: [1,3,5,7,9,11,15],
            cy: [1],
        ),
        \rock2 -> DrumPattern(
            kick: [1,8,9,11],
            sn: [5,13],
            ch: [1,3,5,7,9,11,15],
        ),
        \rock3 -> DrumPattern(
            kick: [1,8,9,11],
            sn: [5,13],
            ch: [1,3,5,7,9,11,15],
            oh: [15],
        ),
        \rock4 -> DrumPattern(
            kick: [1,8,9,11],
            sn: [5,13,15,16],
            ch: [1,3,5,7,9,11,15],
            oh: [15],
        ),
        // electro
        \electro1a -> DrumPattern(
            kick: [1,7],
            sn: [5,13],
        ),
        \electro1b -> DrumPattern(
            kick: [1,7,11,15],
            sn: [5,13],
        ),
        \electro2a -> DrumPattern(
            kick: [1,7],
            sn: [5,13],
        ),
        \electro2b -> DrumPattern(
            kick: [1,11,14],
            sn: [5,13],
        ),
        \electro3a -> DrumPattern(
            kick: [1,7,12],
            sn: [5,13],
        ),
        \electro3b -> DrumPattern(
            kick: [1,7,12,14],
            sn: [5,13],
        ),
        \electro4 -> DrumPattern(
            kick: [1,7,11,14],
            sn: [5,13],
        ),
        \siberiannights -> DrumPattern(
            kick: [1,7],
            sn: [5,13],
            ch: [1,3,4,5,7,8,9,11,12,13,15,16],
        ),
        \newwave -> DrumPattern(
            kick: [1,7,9,10],
            sn: [5,13],
            ch: (1..16),
            oh: [3],
            sh: [5,13],
        ),
        // house
        \house -> DrumPattern(
            kick: [1,5,9,13],
            sn: [5,13],
            oh: [3,7,11,15],
            cym: [1],
        ),
        \house2 -> DrumPattern(
            kick: [1,5,9,13],
            sn: [5,13],
            ch: (1..16),
            oh: [3,6,11,14],
        ),
        \brithouse -> DrumPattern(
            kick: [1,5,9,13],
            ch: [1,2,4,5,6,8,9,10,12,13,14,16],
            oh: [3,6,11,15],
            cl: [5,13],
            cym: [3,7,11,15],
        ),
        \frenchhouse -> DrumPattern(
            kick: [1,5,9,13],
            ch: (1..16),
            oh: [2,4,6,8,10,12,14,16],
            cl: [5,13],
            sh: [1,2,3,5,7,8,9,10,11,13,15,16],
        ),
        \dirtyhouse -> DrumPattern(
            acc: [3,16],
            kick: [1,3,5,9,11,13,16],
            sn: [5,13],
            ch: [11,16],
            oh: [3,11,15],
            cl: [3,5,9,11,13],
        ),
        \deephouse -> DrumPattern(
            kick: [1,5,9,13],
            ch: [2,8,10],
            oh: [3,7,11,15],
            cl: [5,13],
        ),
        \deeperhouse -> DrumPattern(
            kick: [1,5,9,13],
            oh: [3,7,11,12,15],
            cl: [2,10],
            sh: [4,9],
            mt: [3,8,11],
        ),
        \slowdeephouse -> DrumPattern(
            kick: [1,5,9,13],
            ch: [1,5,9,13],
            oh: [3,4,7,8,10,11,13],
            cl: [5,13],
            sh: (1..16),
        ),
        \footworka -> DrumPattern(
            kick: [1,4,7,9,12,15],
            ch: [3,11],
            rim: (1..16),
            cl: [13],
        ),
        \footworkb -> DrumPattern(
            kick: [1,4,7,9,12,15],
            ch: [3,7,8,11,15],
            rim: (1..16),
            cl: [13],
        ),
        // funk
        \amena -> DrumPattern(
            kick: [1,3,11,12],
            sn: [5,8,10,13,16],
            ch: [1,3,5,7,9,11,13,15],
        ),
        \amenb -> DrumPattern(
            kick: [1,3,11,12],
            sn: [8,10,13,16],
            ch: [1,3,5,7,9,11,13,15],
            rim: [5],
        ),
        \amenc -> DrumPattern(
            kick: [1,3,11],
            sn: [8,10,13,16],
            ch: [1,3,5,7,9,11,13,15],
            rim: [15],
        ),
        \amend -> DrumPattern(
            kick: [1,3,11],
            sn: [2,5,8,10,15],
            ch: [1,3,5,7,9,13,15],
            cym: [11],
        ),
        \funky -> DrumPattern(
            kick: [1,3,7,11,14],
            sn: [5,8,10,12,13,16],
            ch: [1,2,3,4,5,6,7,9,10,11,12,13,15,16],
            oh: [8,14],
        ),
        \impeach -> DrumPattern(
            kick: [1,8,9,15],
            sn: [5,13],
            ch: [1,3,5,7,8,9,13,15],
            oh: [11],
        ),
        \levee -> DrumPattern(
            kick: [1,2,8,11,12],
            sn: [5,13],
            ch: [1,3,5,7,9,11,13,15],
        ),
        \newday -> DrumPattern(
            kick: [1,3,8,11,12,16],
            sn: [5,13],
            ch: [1,3,5,7,9,11,13,15],
        ),
        \bigbeat -> DrumPattern(
            kick: [1,4,7,9],
            sn: [5,13],
            ch: [5,13],
        ),
        \ashley -> DrumPattern(
            kick: [1,3,7,9,10],
            sn: [5,13],
            ch: [1,3,5,7,9,13,15],
            oh: [11],
            bell: [1,3,5,7,9,11,13,15],
        ),
        \papa -> DrumPattern(
            kick: [1,8,9,11,16],
            sn: [5,13],
            ch: [5,9,11,13,15,16],
            cym: [5],
        ),
        \superstition -> DrumPattern(
            kick: [1,5,9,13],
            sn: [5,13],
            ch: [1,3,5,7,8,9,10,11,13,15,16],
        ),
        // \buleria -> DrumPattern(
        //     kick: [1,5,9],
        //     ch: [4,9,11],
        //     oh: [1,7],
        //     cl: [2,10],
        //     rim: [5,8,12],
        //     size: 12,
        // ),
        \buleria -> DrumPattern(
            kick: [3,6,8,10,12],
			sn: [1,2,4,5,7,11],
            // ch: [1,2,4,5,7,8,10,11,13,14,16,17,19,20,22,23,25,26,28,29,31,32,34,35],
            // oh: [3,6,9,12,15,18,21,24,27,30,33,36],
            cl: [3,7,8,10,12],
            rim: [1,2,9,11],
            size: 12,
        ),
        \solea -> DrumPattern(
            acc: [1,4,11],
			kick: [1,4,11],
			sn: [2,3,5,6,7,8,9,10,12],
            // ch: [1,2,4,5,7,8,10,11,13,14,16,17,19,20,22,23,25,26,28,29,31,32,34,35],
            // oh: [3,6,9,12,15,18,21,24,27,30,33,36],
            // cl: [3,6,8,10,12]*3,
            // rim: [3,6,8,10,12]*3,
            size: 12,
        ),
        \songformyfather -> DrumPattern(
            kick: [1,7,13,15],
            // ch: [4,9,11],
            // oh: [1,7],
            // cl: [2,10],
            // rim: [5,8,12],
            // size: 12,
        ),
		\lentejas -> DrumPattern(
            kick: [1,3,9,11,17,19,25,27],
			sn: [5,13,21,23,29],
			ch:	(1..8)*4,
			acc: [3,13,23,29],
			size: 32,
        ),
		// from Blue Rondo a la Turk by Dave Brubeck - turkish rhythm
		\turk -> DrumPattern(
            kick: [1,3,5,7,10,12,14,16,19,21,23,25,28,31,34],
				// sn: [5,13,21,23,29],
			ch:	[2,4,6,8,9,11,13,15,17,18,20,22,24,26,27,29,30,32,33,35,36],
			acc: [1,28],
			size: 36,
        ),
        \motorik -> DrumPattern(
            kick: [1,2,4],
            sn: [3],
            ch: [1,2,3,4],
			size: 4,
        ),
        // template
        // \patternname -> DrumPattern(
        //     kick: [0],
        //     sn: [0],
        //     ch: [0],
        //     oh: [0],
        //     rim: [0],
        //     cym: [0],
        //     bell: [0],
        //     cl: [0],
        //     sh: [0],
        //     ht: [0],
        //     mt: [0],
        //     lt: [0],
        // ),
		];

		// all = all.freezeAsParent;
	}
}

+Array {
	asDurs {
		^DrumPattern.asDurs(this);
	}
}