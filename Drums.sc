// Play MIDI drums using DrumPattern
//
// Usage:
//
// MIDIClient.init;
//
// ////// if no instrument (Pbind()) is passed it defaults to
// ////// a midi Pbind() on MIDIOut(0)
// d = Drums(\house);
// ////// or set it to whatever you want
// m = MIDIOut(0);
// d = Drums(\house, Pbind(\type, \midi, \midiout, m, \octave, 3, \amp, 1));
//
// d.play
//
// ///// modify the default instrument
// d.set(\dur, 1/4);
// d.set(\amp, 0.5);
// d.set(\dur, 1/2, \r, Bjorklund(7,12).replace(0,\r).pseq, \tempo, 128/60);
//
// d.reset;
//
// d.mute;
// d.setpattern(\ashley);
// d.setpattern(\brithouse);
//
// ///// play only some instruments
// d.mute(\kick)
// d.solo(\kick)
// d.unmute
//
// d.stop
// d.clear

Drums : Pdef {
	// var <>pdef, <>patterns, <>instrument;
	var <>patterns, <>instrument, <>pairs;
	classvar
	kick,
	sn,
	sne, // snare edge
	ch,
	ph, // pedal hihat
	soh, // semi-open hihat
	oh,
	rim,
	cym1,
	cym1ck, // cym 1 choked
	cym2,
	cym2ck,
	cym3,
	rs, // ride cymbal shank
	rt, // ride cymbal tip
	rck, // ride cymbal choked
	rbl, // ride cymbal bell
	spl, // splash cym
	cl,
	tb, // tambourine
	bell,
	sh, // shakers - maracas
	ht,
	hte, // ht edge
	lt,
	lte; // lt edge

	*initClass {
		kick = 0;
		sn = 2;
		sne = 4; // snare edge
		ch = 6;
		ph = 8; // pedal hihat
		soh = 10; // semi-open hihat
		oh = 12;
		rim = 1;
		cym1 = 13;
		cym1ck = 14; // cym 1 choked
		cym2 = 21;
		cym2ck = 21;
		cym3 = 24;
		rs = 23; // ride cymbal shank
		rt = 15; // ride cymbal tip
		rck = 16; // ride cymbal choked
		rbl = 17; // ride cymbal bell
		spl = 19; // splash cym
		cl = 3;
		tb = 18; // tambourine
		bell = 20;
		sh = 25; // shakers - maracas
		ht = 9;
		hte = 11; // ht edge
		lt = 5;
		lte = 7; // lt edge
	}

	*new { |drumpattern, instrument, pairs|
		^super.new(\drums).init(drumpattern, instrument, pairs);
	}

	init { |drumpattern, instrument, pairs|
		this.setpattern(drumpattern);
		this.setinstrument(instrument);
		this.pairs = pairs ? [\r, 1];

		this.source = this.patterns <> this.instrument;
	}

	// Map all methods that are not understood to a Pbind parameter.
	doesNotUnderstand { |selector ...args|
		this.set(selector, args[0]);
	}

	set { | ...pairs |
		this.pairs = this.pairs ? [\r, 1];
		// replace repeated values, add new ones
		this.pairs = merge(pairs.asDict, this.pairs.asDict, {|new, old| new ? old}).asPairs;
		super.source = this.patterns <> Pbind(*this.pairs) <> this.instrument;
		this.pairs.debug("drums");
	}

	reset {
		this.pairs = [\r, 1];
		this.set();
	}

	setpattern { |drumpattern|
		var pat = DrumPattern.at(drumpattern);
		this.patterns = Ppar([
			Pdef(\kick, Pbind(\note, kick + Pseq(pat.kick.replace(1,0), inf))), // kick
			Pdef(\sn, Pbind(\note, sn + Pseq(pat.sn.replace(1,0), inf))),
			// Pdef(\sne, Pbind(\note, sne + Pseq(pat.sne.replace(1,0), inf))),
			Pdef(\ch, Pbind(\note, ch + Pseq(pat.ch.replace(1,0), inf))),
			// Pdef(\ph, Pbind(\note, ph + Pseq(pat.ph.replace(1,0), inf))),
			Pdef(\oh, Pbind(\note, oh + Pseq(pat.oh.replace(1,0), inf))),
			// Pdef(\soh, Pbind(\note, soh + Pseq(pat.soh.replace(1,0), inf))),
			Pdef(\rim, Pbind(\note, rim + Pseq(pat.rim.replace(1,0).replace(1,0), inf))),
			Pdef(\cym1, Pbind(\note, cym1 + Pseq(pat.cym.replace(1,0), inf))),
			// Pdef(\cym1ck, Pbind(\note, cym1ck + Pseq(pat.cym.replace(1,0), inf))),
			// Pdef(\cym2, Pbind(\note, cym2 + Pseq(pat.cym.replace(1,0), inf))),
			// Pdef(\cym2ck, Pbind(\note, cym2ck + Pseq(pat.cym.replace(1,0), inf))),
			// Pdef(\rs, Pbind(\note, rs + Pseq(pat.rs.replace(1,0), inf))),
			// Pdef(\rt, Pbind(\note, rt + Pseq(pat.rt.replace(1,0), inf))),
			// Pdef(\rck, Pbind(\note, rck + Pseq(pat.rck.replace(1,0), inf))),
			// Pdef(\rbl, Pbind(\note, rbl + Pseq(pat.rbl.replace(1,0), inf))),
			// Pdef(\spl, Pbind(\note, spl + Pseq(pat.spl.replace(1,0), inf))),
			Pdef(\bell, Pbind(\note, bell + Pseq(pat.bell.replace(1,0), inf))),
			Pdef(\cl, Pbind(\note, cl + Pseq(pat.cl.replace(1,0), inf))),
			Pdef(\sh, Pbind(\note, sh + Pseq(pat.sh.replace(1,0), inf))),
			Pdef(\ht, Pbind(\note, ht + Pseq(pat.ht.replace(1,0), inf))),
			// Pdef(\hte, Pbind(\note, hte + Pseq(pat.hte.replace(1,0), inf))),
			Pdef(\mt, Pbind(\note, lte + Pseq(pat.mt.replace(1,0), inf))),
			Pdef(\lt, Pbind(\note, lt + Pseq(pat.lt.replace(1,0), inf))),
		]);
		this.set();
		^this;
	}

	setinstrument { |instrument|
		this.instrument = Pbind(
			\type, \midi,
			\midiout, MIDIOut(0),
			\dur, 1/4,
			\octave, 3,
			\amp, 1,
		);
	}

	mute { |...items|
		this.unmute;
		items.do{|item|
			Pdef(item).source = Pset(\r, \r, Pdef(item).source);
		}
	}

	unmute {
		this.patterns.list.do{|item|
			item.source = Pset(\r, 1, item.source);
		};
	}

	solo { |...items|
		this.patterns.list.do{|item|
			item.source = Pset(\r, \r, item.source);
		};
		items.do{|item|
			Pdef(item).source = Pset(\r, 1, Pdef(item).source);
		}
	}

}