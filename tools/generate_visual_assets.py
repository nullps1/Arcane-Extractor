"""Original M7 pixel art and cuboid model. Run with Python 3 + Pillow.

No downloaded art; fixed seeds make generated resources reproducible.
Coordinates deliberately preserve the M6 menu slot contract.
"""
from pathlib import Path
import json
import random
from PIL import Image, ImageDraw

ROOT = Path(__file__).resolve().parents[1]
ASSETS = ROOT / 'src/main/resources/assets/arcane_extraction'


def save(image, name):
    path = ASSETS / 'textures' / name
    path.parent.mkdir(parents=True, exist_ok=True)
    image.save(path)


def write_json(path, data):
    (ASSETS / path).write_text(json.dumps(data, indent=2) + '\n', encoding='utf-8')


def stone(seed, palette):
    rng = random.Random(seed)
    image = Image.new('RGB', (16, 16))
    for y in range(16):
        for x in range(16):
            image.putpixel((x, y), rng.choice(palette))
    return image


obsidian = stone(7, [(27, 24, 36), (32, 28, 43), (37, 32, 49), (43, 37, 56)])
d = ImageDraw.Draw(obsidian)
for line in [[(0, 5), (4, 5), (7, 2)], [(9, 15), (9, 11), (13, 7), (15, 7)], [(0, 13), (4, 9), (6, 9)]]:
    d.line(line, fill='#514262')
save(obsidian, 'block/extractor_obsidian.png')

trim = stone(17, [(63, 62, 74), (67, 66, 78), (71, 70, 83)])
d = ImageDraw.Draw(trim)
d.rectangle((0, 0, 15, 15), outline='#252330')
d.line([(1, 14), (1, 1), (14, 1)], fill='#93909f')
d.line([(2, 14), (14, 14), (14, 2)], fill='#363340')
for x, y in [(3, 3), (12, 3), (3, 12), (12, 12)]:
    d.point((x, y), fill='#b9b2c9')
save(trim, 'block/extractor_trim.png')

rune = obsidian.copy()
d = ImageDraw.Draw(rune)
d.rectangle((0, 0, 15, 15), outline='#797185')
d.rectangle((2, 2, 13, 13), outline='#44364f')
d.line([(7, 2), (7, 4), (3, 8), (7, 12), (7, 14)], fill='#9a6cc2')
d.line([(8, 2), (8, 4), (12, 8), (8, 12), (8, 14)], fill='#d8acf3')
d.line((5, 8, 10, 8), fill='#ead5ff')
save(rune, 'block/extractor_rune.png')

crystal = Image.new('RGB', (16, 16), '#654384')
d = ImageDraw.Draw(crystal)
d.polygon([(0, 0), (15, 0), (10, 5), (5, 6)], fill='#c99be6')
d.polygon([(0, 0), (5, 6), (4, 12), (0, 15)], fill='#9265bc')
d.polygon([(15, 0), (15, 15), (10, 11), (10, 5)], fill='#8953ac')
d.polygon([(5, 6), (10, 5), (10, 11), (4, 12)], fill='#e7c8ff')
d.line([(1, 1), (14, 1)], fill='#f4e4ff')
save(crystal, 'block/extractor_amethyst.png')

deck = obsidian.copy()
d = ImageDraw.Draw(deck)
d.rectangle((1, 1, 14, 14), outline='#8b8197')
d.line((3, 5, 12, 5), fill='#a77ccd')
d.line((3, 10, 12, 10), fill='#a77ccd')
d.line((7, 3, 7, 12), fill='#dfc3f4')
d.line((8, 3, 8, 12), fill='#75538e')
save(deck, 'block/extractor_deck.png')

elements = []


def box(name, start, end, material, overrides=None):
    faces = {face: {'uv': [0, 0, 16, 16], 'texture': '#' + material}
             for face in ['north', 'south', 'east', 'west', 'up', 'down']}
    for face, texture in (overrides or {}).items():
        faces[face]['texture'] = '#' + texture
    elements.append({'name': name, 'from': start, 'to': end, 'faces': faces})


box('plinth', [1, 0, 2], [15, 2, 14], 'obsidian', {'up': 'trim'})
box('left upright', [2, 2, 8], [4, 13, 12], 'trim')
box('right upright', [12, 2, 8], [14, 13, 12], 'trim')
box('lower housing', [4, 5, 8], [12, 6, 12], 'obsidian')
box('standing housing', [4, 6, 8], [12, 14, 12], 'obsidian', {'south': 'rune'})
box('crown', [4, 14, 8], [12, 15, 12], 'trim')
box('front rune plate', [4, 6, 7.5], [12, 14, 8], 'rune')
box('lens bezel', [5, 8, 7], [11, 12, 7.5], 'trim')
box('amethyst focus', [6, 9, 6.5], [10, 11, 7], 'amethyst')
box('working shelf', [3, 4, 2], [13, 5, 8], 'trim', {'up': 'deck'})
box('shelf support', [6, 2, 5], [10, 4, 8], 'obsidian')

write_json('models/block/arcane_extractor.json', {
    'parent': 'minecraft:block/block', 'ambientocclusion': True,
    'textures': {**{name: 'arcane_extraction:block/extractor_' + name
                   for name in ['obsidian', 'trim', 'rune', 'amethyst', 'deck']},
                 'particle': 'arcane_extraction:block/extractor_obsidian'},
    'elements': elements,
    'display': {
        'gui': {'rotation': [30, 135, 0], 'translation': [0, 0, 0], 'scale': [0.75, 0.75, 0.75]},
        'ground': {'rotation': [0, 0, 0], 'translation': [0, 3, 0], 'scale': [0.3, 0.3, 0.3]},
        'fixed': {'rotation': [0, 180, 0], 'translation': [0, 0, 0], 'scale': [0.6, 0.6, 0.6]}
    }
})
write_json('blockstates/arcane_extractor.json', {'variants': {
    'facing=' + direction: {'model': 'arcane_extraction:block/arcane_extractor', 'y': rotation}
    for direction, rotation in [('north', 0), ('east', 90), ('south', 180), ('west', 270)]
}})

# 1:1 GUI pixels. Text and dynamic highlights are rendered by Minecraft.
gui = Image.new('RGBA', (256, 256), (0, 0, 0, 0))
d = ImageDraw.Draw(gui)
d.rectangle((0, 0, 175, 232), fill='#17141f', outline='#100e16')
d.rectangle((1, 1, 174, 231), outline='#8c809d')
d.rectangle((2, 2, 173, 230), outline='#453951')
d.line((4, 17, 107, 17), fill='#675273')
d.line((4, 18, 107, 18), fill='#27202e')


def panel(bounds):
    d.rectangle(bounds, fill='#241e2e', outline='#493b58')
    x0, y0, x1, y1 = bounds
    d.line((x0+1, y1-1, x1-1, y1-1), fill='#17131e')


panel((7, 21, 99, 54))
panel((112, 24, 170, 62))
panel((7, 66, 99, 136))
for y in [69, 93]:
    d.rectangle((9, y, 97, y+11), fill='#2c2437')
d.line((9, 120, 97, 120), fill='#493b58')
d.line((7, 147, 168, 147), fill='#675273')


def slot(x, y, output=False):
    d.rectangle((x, y, x+17, y+17), fill='#110f18', outline='#110e19')
    d.line([(x, y+17), (x, y), (x+17, y)], fill='#72647f' if output else '#5d526b')
    d.line([(x+1, y+17), (x+17, y+17), (x+17, y+1)], fill='#9e85b5' if output else '#81738e')
    d.rectangle((x+1, y+1, x+16, y+16), fill='#211b2a')


slot(19, 34)
slot(55, 34)
for i in range(6):
    slot(115+(i % 3)*18, 25+(i//3)*18, True)
for row in range(3):
    for col in range(9):
        slot(7+col*18, 150+row*18)
for col in range(9):
    slot(7+col*18, 208)
for x in [43, 85]:
    d.line((x-3, 42, x+3, 42), fill='#b8a1c9')
    d.line([(x+1, 40), (x+3, 42), (x+1, 44)], fill='#ead3fa')
for x, y in [(4, 4), (169, 4), (4, 225), (169, 225)]:
    d.line([(x, y+2), (x+1, y), (x+2, y+2), (x+1, y+4), (x, y+2)], fill='#b998d0')
save(gui, 'gui/arcane_extractor.png')
print('Generated 5 block textures, GUI atlas, model and four blockstates.')
